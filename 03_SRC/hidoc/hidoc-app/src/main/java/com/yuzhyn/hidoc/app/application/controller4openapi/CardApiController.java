package com.yuzhyn.hidoc.app.application.controller4openapi;

import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.hidoc.app.application.entity.card.CardLevel;
import com.yuzhyn.hidoc.app.application.entity.file.ShareFileView;
import com.yuzhyn.hidoc.app.application.service.card.CardUserService;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("openapi/card")
public class CardApiController {

    @Autowired
    CardUserService cardUserService;

    @PostMapping("applyLevel")
    public ResponseData applyLevel(@RequestBody Map<String, Object> params) {
        Tuple2<Boolean, String> result = cardUserService.applyLevel(params);
        ResponseData responseData = ResponseData.ok();
        responseData.putDataMap("flag", result.getT1());
        responseData.putDataMap("msg", result.getT2());
        return responseData;
    }

    @PostMapping("loginLevel")
    public ResponseData loginLevel(@RequestBody Map<String, Object> params) {
        Tuple3<Boolean, String, List<CardLevel>> result = cardUserService.loginLevel(params);
        ResponseData responseData = ResponseData.ok();
        responseData.putDataMap("flag", result.getT1());
        responseData.putDataMap("msg", result.getT2());
        boolean haslevels = ListTool.ok(result.getT3());
        responseData.putDataMap("hasLevels", haslevels);
        if (haslevels) responseData.putDataMap("levels", result.getT3());
        return responseData;
    }

    @PostMapping("lock")
    public ResponseData lock(@RequestBody Map<String, Object> params) {
        Tuple3<Boolean, String, String> result = cardUserService.lock(params);
        ResponseData responseData = ResponseData.ok();
        responseData.putDataMap("flag", result.getT1());
        responseData.putDataMap("key", result.getT2());
        responseData.putDataMap("msg", result.getT3());
        return responseData;
    }

    @PostMapping("unlock")
    public ResponseData unlock(@RequestBody Map<String, Object> params) {
        Tuple2<Boolean, String> result = cardUserService.unlock(params);
        ResponseData responseData = ResponseData.ok();
        responseData.putDataMap("flag", result.getT1());
        responseData.putDataMap("msg", result.getT2());
        return responseData;
    }
}
