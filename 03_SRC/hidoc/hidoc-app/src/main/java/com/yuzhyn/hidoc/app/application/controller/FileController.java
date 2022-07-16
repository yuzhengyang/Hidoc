package com.yuzhyn.hidoc.app.application.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.doc.DocLite;
import com.yuzhyn.hidoc.app.application.entity.file.File;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursor;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUser;
import com.yuzhyn.hidoc.app.application.mapper.file.FileBucketMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileCursorMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserFileConfMapper;
import com.yuzhyn.hidoc.app.application.service.FileService;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import reactor.util.function.Tuple2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping({"f", "file"})
public class FileController {

    @Autowired
    FileService fileService;

    @Autowired
    SysUserFileConfMapper sysUserFileConfMapper;

    @Autowired
    FileBucketMapper fileBucketMapper;

    @Autowired
    FileCursorMapper fileCursorMapper;

    @Autowired
    FileMapper fileMapper;

    //region 查看文件列表

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
        IPage<File> sysFilePage = fileMapper.selectPage(new Page<File>(current, size), null);
        List<File> list = sysFilePage.getRecords();
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
            if (curUser != null && fileService.checkSpaceLimit(curUser.getId(), files[0].getSize())) {
                List<FileCursor> sysFileList = fileService.uploadFiles(collectedId, expiryTime, bucketName, files, curUser);
                return ResponseData.okData(sysFileList);
            } else {
                return ResponseData.error("用户无权限或空间受限");
            }
        }
        return ResponseData.error("请选择文件");
    }
    //endregion

    /**
     * 删除文件（后续动作由定时任务执行，删除超期的文件，并释放空间，另外注意上传时，如果有文件指纹相同的已删除文件，要注意处理）
     *
     * @param params
     * @return
     */
    @Transactional
    @PostMapping("delete")
    public ResponseData delete(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "cursorId", "fileId")) {
            // 获取指针ID和文件ID
            String cursorId = MapTool.get(params, "cursorId", "").toString();
            String fileId = MapTool.get(params, "fileId", "").toString();
            if (StringTool.ok(cursorId, fileId)) {

                File file = fileMapper.selectById(fileId);
                FileCursor cursor = fileCursorMapper.selectById(cursorId);
                Long cursorCount = fileCursorMapper.selectCount(new LambdaQueryWrapper<FileCursor>().eq(FileCursor::getFileId, fileId).eq(FileCursor::getIsDelete, false));

                if (cursorCount == 1) {
                    // 如果文件仅有一个指针指向（未删除的），则标记指针和文件都为删除状态
                    file.setIsDelete(true);
                    file.setDeleteUserId(CurrentUserManager.getUserId());
                    file.setDeleteTime(LocalDateTime.now());
                    fileMapper.updateById(file);

                    cursor.setIsDelete(true);
                    cursor.setDeleteUserId(CurrentUserManager.getUserId());
                    cursor.setDeleteTime(LocalDateTime.now());
                    fileCursorMapper.updateById(cursor);
                } else {
                    // 如果文件包含多个指针指向，则只标记指针为删除状态
                    cursor.setIsDelete(true);
                    cursor.setDeleteUserId(CurrentUserManager.getUserId());
                    cursor.setDeleteTime(LocalDateTime.now());
                    fileCursorMapper.updateById(cursor);
                }
            }
        }
        return ResponseData.ok();
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
        Tuple2<FileCursor, File> fileInfo = fileService.getDownloadFile(userPrefix, bucketName, fileName);
        fileService.download(fileInfo.getT1(), fileInfo.getT2(), request, response);
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
        Tuple2<FileCursor, File> fileInfo = fileService.getDownloadFileByCursor(cursorId);
        fileService.download(fileInfo.getT1(), fileInfo.getT2(), request, response);
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
        FileCursor cacheCursor = R.Caches.SysFileCursor.get(uname);
        File cacheFile = R.Caches.SysFile.get(uname);

        if (cacheCursor != null && cacheFile != null) {
            // 存在缓存直接走缓存
            fileService.download(cacheCursor, cacheFile, request, response);
        } else {
            // 不存在缓存，走查询，然后加入到缓存中
            Tuple2<FileCursor, File> fileInfo = fileService.getDownloadFileByUname(uname);
            if (fileInfo.getT1() != null && fileInfo.getT2() != null) {
                R.Caches.SysFileCursor.put(uname, fileInfo.getT1());
                R.Caches.SysFile.put(uname, fileInfo.getT2());
            }
            fileService.download(fileInfo.getT1(), fileInfo.getT2(), request, response);
        }
    }
    //endregion
}
