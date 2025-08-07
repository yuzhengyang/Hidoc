package com.yuzhyn.hidoc.app.application.controller4openapi;

import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.utils.SseTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("openapi/appconf")
public class AppConfApiController {
    @GetMapping("function")
    public ResponseData hello() {
        List<String> list = new ArrayList<>();
        String s = MapTool.getString(R.Maps.functionConfig, "display", "");
        String[] sArray = StringTool.split(s, ",", true, true, true);
        if (ListTool.ok(sArray)) list.addAll(Arrays.asList(sArray));
        return ResponseData.okData(list);
    }
}