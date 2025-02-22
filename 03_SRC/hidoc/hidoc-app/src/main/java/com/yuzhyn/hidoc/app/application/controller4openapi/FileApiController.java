package com.yuzhyn.hidoc.app.application.controller4openapi;

import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.hidoc.app.application.entity.file.File;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursor;
import com.yuzhyn.hidoc.app.application.entity.file.HidocFileView;
import com.yuzhyn.hidoc.app.application.entity.file.ShareFileView;
import com.yuzhyn.hidoc.app.application.mapper.file.FileCursorMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.ShareFileViewMapper;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
