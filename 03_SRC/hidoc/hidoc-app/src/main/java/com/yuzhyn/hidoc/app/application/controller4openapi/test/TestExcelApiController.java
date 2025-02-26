package com.yuzhyn.hidoc.app.application.controller4openapi.test;

import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import com.yuzhyn.hidoc.app.application.entity.sys.SysAccessLog;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysAccessLogMapper;
import com.yuzhyn.hidoc.app.application.service.test.TestEasyExcelService;
import com.yuzhyn.hidoc.app.application.service.test.TestPoiService;
import com.yuzhyn.hidoc.app.application.service.test.TestService;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cursor.Cursor;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("x-openapi/testexcel")
public class TestExcelApiController {

    @Autowired
    TestPoiService testPoiService;

    @Autowired
    TestEasyExcelService testEasyExcelService;

    String DIR_ROOT = "D:\\temp\\";

    @GetMapping("poi")
    public ResponseData poi() {
        // 记录开始时间
        LocalDateTime startTime = LocalDateTime.now();
        System.out.println("开始时间：" + startTime);

        String datetime = DateTimeFormat.toStr(LocalDateTime.now(), DateTimeFormatPattern.SHORT_DATETIME);
        testPoiService.writeExcel(DIR_ROOT + "poi-" + datetime + ".xlsx");

        // 记录结束时间和持续时间
        LocalDateTime endTime = LocalDateTime.now();
        System.out.println("结束时间：" + endTime);
        System.out.println("持续时间：" + Duration.between(startTime, endTime).toMillis() + "毫秒");

        ResponseData responseData = ResponseData.ok();
        return responseData;
    }


    @GetMapping("easyexcel")
    public ResponseData easyexcel() {
        // 记录开始时间
        LocalDateTime startTime = LocalDateTime.now();
        System.out.println("开始时间：" + startTime);

        String datetime = DateTimeFormat.toStr(LocalDateTime.now(), DateTimeFormatPattern.SHORT_DATETIME);
        testEasyExcelService.writeExcel(DIR_ROOT + "easyexcel-" + datetime + ".xlsx");

        // 记录结束时间和持续时间
        LocalDateTime endTime = LocalDateTime.now();
        System.out.println("结束时间：" + endTime);
        System.out.println("持续时间：" + Duration.between(startTime, endTime).toMillis() + "毫秒");

        ResponseData responseData = ResponseData.ok();
        return responseData;
    }
}
