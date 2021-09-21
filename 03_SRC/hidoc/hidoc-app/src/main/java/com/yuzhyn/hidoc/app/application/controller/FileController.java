package com.yuzhyn.hidoc.app.application.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuzhyn.azylee.core.ios.dirs.DirTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.file.File;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursor;
import com.yuzhyn.hidoc.app.application.entity.sys.SysAccessLog;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUser;
import com.yuzhyn.hidoc.app.application.mapper.file.FileBucketMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileCursorMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysAccessLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserFileConfMapper;
import com.yuzhyn.hidoc.app.application.service.FileService;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.datetimes.LocalDateTimeTool;
import com.yuzhyn.azylee.core.datas.ids.UUIDTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Autowired
    SysAccessLogMapper sysAccessLogMapper;

    //region 查看文件列表

    /**
     * 查看文件列表
     *
     * @return
     */
    @GetMapping({"list", "l"})
    public ResponseData list() {
        List<File> list = fileMapper.selectList(null);
        return ResponseData.okData(list);
    }

    @GetMapping({"export"})
    public ResponseData export() {

        String fileName = DirTool.combine(R.Paths.Temp, UUIDTool.get() + ".xlsx");
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        ExcelWriter excelWriter = EasyExcel.write(fileName, SysAccessLog.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("Sheet1").head(SysAccessLog.class).build();

        //region 分批处理
        for (int i = 0; i < 500; i++) {
            List<SysAccessLog> list = sysAccessLogMapper.selectList(null);
            fillData(list);
            excelWriter.write(list, writeSheet);
        }
        //endregion

//        //region 全量处理
//        List list = new ArrayList();
//        for (int i = 0; i < 500; i++) {
//            List<SysAccessLog> _l = sysAccessLogMapper.selectList(null);
//            list.addAll(_l);
//        }
//        fillData(list);
//        excelWriter.write(list, writeSheet);
//        //endregion

        excelWriter.finish();

        return ResponseData.ok();
    }

    private List fillData(List<SysAccessLog> list) {
        for (SysAccessLog item : list) {
            item.setS1(UUIDTool.get());
            item.setS2(UUIDTool.get());
            item.setS3(UUIDTool.get());
            item.setS4(UUIDTool.get());
            item.setS5(UUIDTool.get());
            item.setS6(UUIDTool.get());
            item.setS7(UUIDTool.get());
            item.setS8(UUIDTool.get());
            item.setS9(UUIDTool.get());
            item.setS10(UUIDTool.get());
            item.setS11(UUIDTool.get());
            item.setS12(UUIDTool.get());
            item.setS13(UUIDTool.get());
            item.setS14(UUIDTool.get());
            item.setS15(UUIDTool.get());
            item.setS16(UUIDTool.get());
            item.setS17(UUIDTool.get());
            item.setS18(UUIDTool.get());
            item.setS19(UUIDTool.get());
            item.setS20(UUIDTool.get());
        }
        return list;
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
                List<FileCursor> sysFileList = new ArrayList<>();
                if (null == bucketName) bucketName = UUIDTool.get();
                if (null == expiryTime) expiryTime = LocalDateTimeTool.max();

                for (MultipartFile file : files) {
                    if (StringTool.ok(bucketName) && expiryTime != null) {
                        File sysFile = fileService.preCreateSysFile(file, curUser.getId());
                        if (sysFile != null) {
                            Tuple3<Boolean, Boolean, File> saveToDisk = fileService.saveToDisk(file, sysFile);
                            File existFile = null;
                            if (saveToDisk.getT2()) existFile = saveToDisk.getT3();
                            if (saveToDisk.getT1()) {
                                FileCursor cursor = fileService.saveDb(sysFile, existFile, bucketName, collectedId, expiryTime);
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
        FileCursor cacheCursor = R.Cache.SysFileCursor.get(uname);
        File cacheFile = R.Cache.SysFile.get(uname);

        if (cacheCursor != null && cacheFile != null) {
            // 存在缓存直接走缓存
            fileService.download(cacheCursor, cacheFile, request, response);
        } else {
            // 不存在缓存，走查询，然后加入到缓存中
            Tuple2<FileCursor, File> fileInfo = fileService.getDownloadFileByUname(uname);
            if (fileInfo.getT1() != null && fileInfo.getT2() != null) {
                R.Cache.SysFileCursor.put(uname, fileInfo.getT1());
                R.Cache.SysFile.put(uname, fileInfo.getT2());
            }
            fileService.download(fileInfo.getT1(), fileInfo.getT2(), request, response);
        }
    }
    //endregion
}
