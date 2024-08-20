package com.yuzhyn.hidoc.app.utils;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event;

public class SseTool {
    // timeout
    private static Long DEFAULT_TIME_OUT = 5 * 60 * 1000L;

    private static final Map<String, SseEmitter> subscribeMap = new ConcurrentHashMap<>();

    /**
     * 添加订阅
     */
    public static SseEmitter addSub(String subId) {
        if (null == subId || "".equals(subId)) {
            return null;
        }

        SseEmitter emitter = subscribeMap.get(subId);
        if (null == emitter) {
            emitter = new SseEmitter(DEFAULT_TIME_OUT);

            emitter.onTimeout(() -> {
                // 注册超时回调，超时后触发
                System.out.println("onTimeout,subId=" + subId);
                closeSub(subId);
            });

            emitter.onCompletion(() -> {
                // 注册完成回调，调用 emitter.complete() 触发
                System.out.println("onCompletion,subId=" + subId);
                closeSub(subId);
            });
            subscribeMap.put(subId, emitter);
        }
        return emitter;
    }

    public static void pubMsg(String subId, String name, String id, Object msg) {
        SseEmitter emitter = subscribeMap.get(subId);
        if (null != emitter) {
            try {
                System.out.println(msg);
                // 更规范的消息结构看源码
                emitter.send(event().name(name).id(id).data(msg), MediaType.APPLICATION_JSON);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 关闭订阅
    public static void closeSub(String subId) {
        SseEmitter emitter = subscribeMap.get(subId);
        if (null != emitter) {
            try {
                emitter.complete();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                subscribeMap.remove(subId);
            }
        }
    }
}
