package com.yuzhyn.hidoc.app.application.controller4openapi;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.yuzhyn.azylee.core.datas.exceptions.ExceptionTool;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("openapi/log")
public class LogApiController {

    @GetMapping("level/{value}")
    public ResponseData changeLevel(@PathVariable("value") String value) {

        boolean status = false;
        String msg = "";

        System.out.println("log level: " + (log.isTraceEnabled() ? "trace," : "") +
                (log.isDebugEnabled() ? "debug," : "") + (log.isInfoEnabled() ? "info," : "") +
                (log.isWarnEnabled() ? "warn," : "") + (log.isErrorEnabled() ? "error" : ""));

        try {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            loggerContext.getLogger("org.mybatis").setLevel(Level.valueOf(value));
            loggerContext.getLogger("org.springframework").setLevel(Level.valueOf(value));
        } catch (Exception e) {
            status = false;
            msg = ExceptionTool.getStackTrace(e);
        }

        System.out.println("log level: " + (log.isTraceEnabled() ? "trace," : "") +
                (log.isDebugEnabled() ? "debug," : "") + (log.isInfoEnabled() ? "info," : "") +
                (log.isWarnEnabled() ? "warn," : "") + (log.isErrorEnabled() ? "error" : ""));

        status = true;
        msg = "修改完成";

        return ResponseData.ok(msg);
    }
}