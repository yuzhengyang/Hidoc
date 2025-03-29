package com.yuzhyn.hidoc.app.application.controller4openapi;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import com.yuzhyn.azylee.core.datas.exceptions.ExceptionTool;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLite;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserLiteMapper;
import com.yuzhyn.hidoc.app.application.service.sys.AuthCodeService;
import com.yuzhyn.hidoc.app.application.service.sys.EmailService;
import com.yuzhyn.hidoc.app.application.service.sys.SysLockService;
import com.yuzhyn.hidoc.app.common.model.ResponseData;
import com.yuzhyn.hidoc.app.utils.SseTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.util.function.Tuple2;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("openapi/app")
public class AppApiController {

    @Autowired
    SysLockService sysLockService;

    @GetMapping("hello")
    public ResponseData hello() {
        return ResponseData.ok("hello");
    }

    @GetMapping("timestamp")
    public ResponseData timestamp() {
        var ts = System.currentTimeMillis();
        ResponseData responseData = ResponseData.ok();
        responseData.putDataMap("timestamp", ts);
        return responseData;
    }

    @GetMapping(value = "lock/{key}/{sec}")
    public ResponseData lock(@PathVariable String key, @PathVariable Long sec) {
        ResponseData responseData = ResponseData.ok();
        String val = sysLockService.lock(key, sec, "");
        if (ObjectUtil.isNotEmpty(val)) {
            responseData.putDataMap("lock", true);
            responseData.putDataMap("key", key);
            responseData.putDataMap("val", val);
        } else {
            responseData.putDataMap("lock", false);
        }
        return responseData;
    }

    @GetMapping(value = "unlock/{key}/{val}")
    public ResponseData unlock(@PathVariable String key, @PathVariable String val) {
        ResponseData responseData = ResponseData.ok();
        if (sysLockService.unlock(key, val)) {
            responseData.putDataMap("unlock", true);
        } else {
            responseData.putDataMap("unlock", false);
        }
        return responseData;
    }

//    @GetMapping(value = "sse/{id}")
//    public SseEmitter sse(@PathVariable String id) {
//        // 添加订阅,建立sse链接
//        SseEmitter emitter = SseTool.addSub(id);
//        new Thread(() -> {
//            try {
//                for (int i = 0; i < 20; i++) {
//                    // 发送消息
//                    SseTool.pubMsg(id, "", String.valueOf(i), id + " - hmg come " + i);
//                    Thread.sleep(1 * 1000);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                // 消息发送完关闭订阅
//                SseTool.closeSub(id);
//            }
//        }).start();
//        return emitter;
//    }
}