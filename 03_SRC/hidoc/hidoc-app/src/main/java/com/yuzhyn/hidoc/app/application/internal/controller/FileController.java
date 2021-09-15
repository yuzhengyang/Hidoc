package com.yuzhyn.hidoc.app.application.internal.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.internal.entity.SysFile;
import com.yuzhyn.hidoc.app.application.internal.entity.SysFileCursor;
import com.yuzhyn.hidoc.app.application.internal.entity.SysUser;
import com.yuzhyn.hidoc.app.application.internal.mapper.SysFileBucketMapper;
import com.yuzhyn.hidoc.app.application.internal.mapper.SysFileCursorMapper;
import com.yuzhyn.hidoc.app.application.internal.mapper.SysFileMapper;
import com.yuzhyn.hidoc.app.application.internal.mapper.SysUserFileConfMapper;
import com.yuzhyn.hidoc.app.application.internal.service.SysFileService;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import com.yuzhyn.hidoc.app.utils.ClientIPTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pers.yuzhyn.azylee.core.datas.collections.ListTool;
import pers.yuzhyn.azylee.core.datas.collections.MapTool;
import pers.yuzhyn.azylee.core.datas.datetimes.LocalDateTimeTool;
import pers.yuzhyn.azylee.core.datas.ids.UUIDTool;
import pers.yuzhyn.azylee.core.datas.strings.StringTool;
import pers.yuzhyn.azylee.core.ios.dirs.DirTool;
import pers.yuzhyn.azylee.core.ios.files.FileTool;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping({"i/file", "i/f"})
public class FileController {

    @Autowired
    SysFileService sysFileService;

    @Autowired
    SysUserFileConfMapper sysUserFileConfMapper;

    @Autowired
    SysFileBucketMapper sysFileBucketMapper;

    @Autowired
    SysFileCursorMapper sysFileCursorMapper;

    @Autowired
    SysFileMapper sysFileMapper;

    //region 查看文件列表

    /**
     * 查看文件列表
     *
     * @return
     */
    @GetMapping({"list", "l"})
    public ResponseData list() {
        List<SysFile> list = sysFileMapper.selectList(null);
        return ResponseData.okData(list);
    }

    /**
     * 查看文件列表（带分页）
     *
     * @param params
     * @return
     */
    @PostMapping({"pageList", "pl"})
    public ResponseData pageList(@RequestBody Map<String, Object> params) {
        int current = MapTool.getInt(params, "current", 1);
        int size = MapTool.getInt(params, "size", 1);
        IPage<SysFile> sysFilePage = sysFileMapper.selectPage(new Page<SysFile>(current, size), null);
        List<SysFile> list = sysFilePage.getRecords();
        return ResponseData.okData(list);
    }
    //endregion

    //region 上传文件

    /**
     * 上传文件
     *
     * @param expiryTime
     * @param bucketName
     * @param files
     * @return
     */
    @PostMapping({"upload", "u"})
    public ResponseData upload(@RequestParam(value = "collectedId", required = false) String collectedId,
                               @RequestParam(value = "expiryTime", required = false) LocalDateTime expiryTime,
                               @RequestParam(value = "bucketName", required = false) String bucketName,
                               @RequestParam("file") MultipartFile[] files) {
        if (ListTool.ok(files)) {
            SysUser curUser = CurrentUserManager.getUser();
            if (curUser != null && sysFileService.checkSpaceLimit(curUser.getId(), files[0].getSize())) {
                List<SysFileCursor> sysFileList = new ArrayList<>();
                if (null == bucketName) bucketName = UUIDTool.get();
                if (null == expiryTime) expiryTime = LocalDateTimeTool.max();

                for (MultipartFile file : files) {
                    if (StringTool.ok(bucketName) && expiryTime != null) {
                        SysFile sysFile = sysFileService.preCreateSysFile(file, curUser.getId());
                        if (sysFile != null) {
                            Tuple3<Boolean, Boolean, SysFile> saveToDisk = sysFileService.saveToDisk(file, sysFile);
                            SysFile existFile = null;
                            if (saveToDisk.getT2()) existFile = saveToDisk.getT3();
                            if (saveToDisk.getT1()) {
                                SysFileCursor cursor = sysFileService.saveDb(sysFile, existFile, bucketName, collectedId, expiryTime);
                                if (cursor != null) sysFileList.add(cursor);
                            }
                        }
                    }
                }
                return ResponseData.okData(sysFileList);
            } else {
                return ResponseData.error("用户无权限或配额受限");
            }
        }
        return ResponseData.error("请选择文件");
    }
    //endregion

    //region 下载文件

    /**
     * 下载文件（目录路径模式）
     * http://127.0.0.1:24001/i/file/download/sa/apple/sugar_nacos.sql
     * http://127.0.0.1:24001/i/file/download/sa/apple/禅与摩托车维修艺术.pdf
     * http://127.0.0.1:24001/i/f/d/sa/apple/禅与摩托车维修艺术.pdf
     *
     * @param userPrefix
     * @param bucketName
     * @param fileName
     * @param response
     */
    @GetMapping({"download/{userPrefix}/{bucketName}/{fileName}", "d/{userPrefix}/{bucketName}/{fileName}"})
    @ResponseBody
    public void download(@PathVariable String userPrefix, @PathVariable String bucketName, @PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) {
        Tuple2<SysFileCursor, SysFile> fileInfo = sysFileService.getDownloadFile(userPrefix, bucketName, fileName);
        sysFileService.download(fileInfo.getT1(), fileInfo.getT2(), request, response);
    }

    /**
     * 文件下载（id模式）
     * http://127.0.0.1:24001/i/file/download/b3f746673926444f9c4cd071e5befdf1
     * http://127.0.0.1:24001/i/f/d/b3f746673926444f9c4cd071e5befdf1
     *
     * @param cursorId
     * @param response
     */
    @GetMapping({"download/cursor/{cursorId}", "d/c/{cursorId}"})
    @ResponseBody
    public void downloadByCursor(@PathVariable String cursorId, HttpServletRequest request, HttpServletResponse response) {
        Tuple2<SysFileCursor, SysFile> fileInfo = sysFileService.getDownloadFileByCursor(cursorId);
        sysFileService.download(fileInfo.getT1(), fileInfo.getT2(), request, response);
    }

    /**
     * 文件下载（uname模式）
     * http://127.0.0.1:24001/i/file/download/uname/b3f746673926444f9c4cd071e5befdf1.jpg
     * http://127.0.0.1:24001/i/f/d/u/b3f746673926444f9c4cd071e5befdf1.jpg
     *
     * @param uname
     * @param response
     */
    @GetMapping({"download/uname/{uname}", "d/u/{uname}"})
    @ResponseBody
    public void downloadByUname(@PathVariable String uname, HttpServletRequest request, HttpServletResponse response) {
        SysFileCursor cacheCursor = R.Cache.SysFileCursor.get(uname);
        SysFile cacheFile = R.Cache.SysFile.get(uname);

        if (cacheCursor != null && cacheFile != null) {
            // 存在缓存直接走缓存
            sysFileService.download(cacheCursor, cacheFile, request, response);
        } else {
            // 不存在缓存，走查询，然后加入到缓存中
            Tuple2<SysFileCursor, SysFile> fileInfo = sysFileService.getDownloadFileByUname(uname);
            if (fileInfo.getT1() != null && fileInfo.getT2() != null) {
                R.Cache.SysFileCursor.put(uname, fileInfo.getT1());
                R.Cache.SysFile.put(uname, fileInfo.getT2());
            }
            sysFileService.download(fileInfo.getT1(), fileInfo.getT2(), request, response);
        }
    }
    //endregion
}
