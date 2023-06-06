package com.yuzhyn.hidoc.app.application.controller.sys;

import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysStatusRptMapper;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping({"sysstatusrpt"})
public class SysStatusRptController {

    @Autowired
    SysStatusRptMapper sysStatusRptMapper;

    @PostMapping("elapsedTime")
    public ResponseData elapsedTime(@RequestBody Map<String, Object> params) {
        if (MapTool.ok(params, "mode", "date")) {
            String mode = MapTool.getString(params, "mode", "");
            LocalDateTime date = MapTool.getLocalDateTime(params, "date", LocalDateTime.now());
            if (date == null) date = LocalDateTime.now();
            LocalDate beginDate = date.toLocalDate();
            LocalDate endDate = date.plusDays(1).toLocalDate();
            return ResponseData.okData(sysStatusRptMapper.elapsedTime(beginDate, endDate));
        }
        return ResponseData.ok();
    }
}
