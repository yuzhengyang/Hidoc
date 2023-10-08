package com.yuzhyn.hidoc.app.application.controller4openapi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.exceptions.ExceptionTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLiteMapper;
import com.yuzhyn.hidoc.app.application.service.sys.AuthCodeService;
import com.yuzhyn.hidoc.app.application.service.sys.EmailService;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.util.function.Tuple2;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("openapi/app")
public class AppApiController {
    @GetMapping("hello")
    public ResponseData getForRegister() {
        return ResponseData.ok("hello");
    }
}