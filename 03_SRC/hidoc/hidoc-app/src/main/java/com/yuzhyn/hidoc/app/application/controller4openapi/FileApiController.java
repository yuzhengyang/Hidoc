package com.yuzhyn.hidoc.app.application.controller4openapi;

import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.exceptions.ExceptionTool;
import com.yuzhyn.azylee.core.ios.dirs.DirTool;
import com.yuzhyn.azylee.core.ios.files.FileTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.file.File;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursor;
import com.yuzhyn.hidoc.app.application.entity.file.FileDownloadLog;
import com.yuzhyn.hidoc.app.application.entity.file.HidocFileView;
import com.yuzhyn.hidoc.app.application.entity.file.ShareFileView;
import com.yuzhyn.hidoc.app.application.mapper.file.FileCursorMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.ShareFileViewMapper;
import com.yuzhyn.hidoc.app.application.service.file.FileService;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import com.yuzhyn.hidoc.app.utils.ClientIPTool;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.util.function.Tuple2;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("openapi/file")
public class FileApiController {

    @Autowired
    ShareFileViewMapper shareFileViewMapper;

    @Autowired
    FileCursorMapper fileCursorMapper;

    @Autowired
    FileMapper fileMapper;

    @Autowired
    FileService fileService;

    /**
     * 共享文件列表
     *
     * @param params
     * @return
     */
    @PostMapping("shareList")
    public ResponseData shareList(@RequestBody Map<String, Object> params) {
        int current = MapTool.getInt(params, "current", 1);
        int size = MapTool.getInt(params, "size", 2000);
        ResponseData responseData = ResponseData.ok();


        Long count = shareFileViewMapper.selectFilesCount();
        List<ShareFileView> files = shareFileViewMapper.selectFiles(current, size);
        responseData.putData(files);
        responseData.setTotal(count);
        return responseData;
    }

    /**
     * 文件详情
     *
     * @param params
     * @return
     */
    @PostMapping("detail")
    public ResponseData detail(@RequestBody Map<String, Object> params) {
        ResponseData responseData = ResponseData.ok();
        String id = MapTool.get(params, "id", "").toString();
        FileCursor fileCursor = fileCursorMapper.selectById(id);
        if (fileCursor != null) {
            responseData.putDataMap("fileCursor", fileCursor);
            File file = fileMapper.selectById(fileCursor.getFileId());
            if (file != null) {
                responseData.putDataMap("file", file);
            }
        }
        return responseData;
    }

    /**
     * 服务间文件传输
     * 首先通过服务器密钥，确认服务合法性，服务器密钥存储到表中，定时更新保证安全（服务器更新自己的器密钥）
     * 然后通过文件指针的ID，确认文件信息，下载文件内容
     *
     * @param serverSecretKey 服务器密钥
     * @param cursorId        文件指针ID
     * @param request
     * @param response
     */
    @GetMapping({"transfer/{serverSecretKey}/{cursorId}", "t/{serverSecretKey}/{cursorId}"})
    @ResponseBody
    public void transfer(@PathVariable String serverSecretKey, @PathVariable String cursorId, HttpServletRequest request, HttpServletResponse response) {
        // 查询服务器的密钥，服务器密钥每1分钟更新一次，最多缓存两个密钥信息
        // 如果服务器时间差距超过2分钟，则将导致无法进行文件传输操作
        LocalDateTime serverSecretKeyTime = R.Caches.ServerSecretKey.getIfPresent(serverSecretKey);
        if (serverSecretKeyTime == null) return; // 密钥不存在，则返回错误信息
        if (LocalDateTime.now().isAfter(serverSecretKeyTime.plusMinutes(2))) return; // 密钥过期，则返回错误信息

        // 验证密钥后，通过时方可下载文件
        Tuple2<FileCursor, File> fileInfo = fileService.getDownloadFileByCursor(cursorId);
        fileService.download("FileApiController", fileInfo.getT1(), fileInfo.getT2(), request, response);
    }

//    @GetMapping({"transfer/{serverSecretKey}/{cursorId}", "t/{serverSecretKey}/{cursorId}"})
//    @ResponseBody
//    public ResponseEntity<Resource> transfer(@PathVariable String serverSecretKey, @PathVariable String cursorId, HttpServletRequest request, HttpServletResponse response) {
//        // 查询服务器的密钥，服务器密钥每1分钟更新一次，最多缓存两个密钥信息
//        // 如果服务器时间差距超过2分钟，则将导致无法进行文件传输操作
//        LocalDateTime serverSecretKeyTime = R.Caches.ServerSecretKey.getIfPresent(serverSecretKey);
//        if (serverSecretKeyTime == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 密钥不存在，则返回错误信息
//        if (LocalDateTime.now().isAfter(serverSecretKeyTime.plusMinutes(2))) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 密钥过期，则返回错误信息
//
//        // 验证密钥后，通过时方可下载文件
//        Tuple2<FileCursor, File> fileInfo = fileService.getDownloadFileByCursor(cursorId);
//        try {
//            log.info("download log: " + ClientIPTool.getIp(request) + ", file: " + fileInfo.getT2().getName());
//
//            String pathName = DirTool.combine(R.Paths.SysFile, fileInfo.getT2().getRealPath());
//            log.info("下载文件路径检查：" + pathName);
//
//            if (FileTool.isExist(pathName)) {
//                java.io.File file = new java.io.File(pathName);
//                // 检查文件是否存在
//                if (!file.exists() || !file.isFile()) {
//                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//                }
//
//                // 2. 使用FileSystemResource，关键是不要让它被JSON序列化
//                Resource resource = new FileSystemResource(file);
//
//                // 3. 构建响应头，指定文件名和下载方式
//                String encodedFileName = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8);
//                HttpHeaders headers = new HttpHeaders();
//                // 注意：必须指定Content-Disposition为attachment，触发文件下载
//                headers.add(HttpHeaders.CONTENT_DISPOSITION,
//                        "attachment; filename*=UTF-8''" + encodedFileName); // 支持中文文件名
//                headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()));
//
//                log.info("记录日志");
//                try {
//                    FileDownloadLog log = new FileDownloadLog();
//                    log.setId(R.SnowFlake.nexts());
//                    log.setIp(ClientIPTool.getIp(request));
//                    log.setCreateTime(LocalDateTime.now());
//                    log.setCursorId(fileInfo.getT1().getId());
//                    log.setFileName(fileInfo.getT2().getName());
//                    log.setFileId(fileInfo.getT2().getId());
//                    R.Queues.FileDownloadLogQueue.add(log);
//                } catch (Exception ex) {
//                }
//
//                log.info("返回开始下载");
//                // 4. 返回ResponseEntity，指定媒体类型为二进制流
//                return ResponseEntity.ok()
//                        .headers(headers)
//                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                        .body(resource);
//            }
//        } catch (Exception ex) {
//            log.error(ExceptionTool.getStackTrace(ex));
//        }
//        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
