package com.yuzhyn.hidoc.app.application.controller;

import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocAccessLogMapper;
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
@RequestMapping({"dashboard"})
public class DashboardController {

    @Autowired
    DocAccessLogMapper docAccessLogMapper;

    @PostMapping("myDocReadLog")
    public ResponseData myDocReadLog(@RequestBody Map<String, Object> params) {
        ResponseData responseData = ResponseData.ok();
        String docId = MapTool.getString(params, "docId", "");
        String userId = CurrentUserManager.getUserId();

        List<Map> logList = docAccessLogMapper.myDocReadLog(userId, docId);
        if (ListTool.ok(logList)) responseData.putData(logList);
        return responseData;
    }
}
