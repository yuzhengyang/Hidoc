package com.yuzhyn.hidoc.app.application.controller4openapi;

import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.application.entity.doc.DocLite;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLiteOnline;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocLiteMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLiteOnlineMapper;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.manager.CurrentUserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("openapi/heartbeat")
public class HeartbeatApiController {

    @Autowired
    SysUserLiteOnlineMapper sysUserLiteOnlineMapper;

    @Autowired
    DocLiteMapper docLiteMapper;

    @PostMapping("base")
    public ResponseData base(@RequestBody Map<String, Object> params) {
        ResponseData responseData = ResponseData.ok();
        responseData.setStatus("n");
        try {
            SysUserLiteOnline sysUserLiteOnline = new SysUserLiteOnline();
            sysUserLiteOnline.setId(CurrentUserManager.getUserId());
            sysUserLiteOnline.setOnlineTime(LocalDateTime.now());
            int flag = sysUserLiteOnlineMapper.updateById(sysUserLiteOnline);
            if (flag > 0) responseData.setStatus("y");
        } catch (Exception ex) {
        }
        return responseData;
    }


    @PostMapping("lockDoc")
    public ResponseData lockDoc(@RequestBody Map<String, Object> params) {
        ResponseData responseData = ResponseData.ok();
        responseData.setStatus("n");

        if (!MapTool.ok(params, "id")) return responseData;
        if (!CurrentUserManager.isLogin()) return responseData;

        String id = MapTool.getString(params, "id", "");

        try {
            DocLite docLite = docLiteMapper.selectById(id);
            if (docLite != null && StringTool.ok(docLite.getLockUserId()) && CurrentUserManager.getUserId().equals(docLite.getLockUserId())) {
                docLite.setLockTime(LocalDateTime.now());
                int flag = docLiteMapper.updateById(docLite);
                if (flag > 0) responseData.setStatus("y");
            }
        } catch (Exception ex) {
        }
        return responseData;
    }
}
