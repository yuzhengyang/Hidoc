package com.yuzhyn.hidoc.app.application.controller4openapi;

import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.hidoc.app.application.entity.file.HidocFileView;
import com.yuzhyn.hidoc.app.application.entity.file.ShareFileView;
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
}
