package com.yuzhyn.hidoc.app.manager;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 当前环境配置信息（应用级别）
 */
@Slf4j
public class CurrentEnvironmentManager {
    private static ConcurrentHashMap<String, Object> properties = new ConcurrentHashMap<>();

    public static void add(String key, Object value) {
        properties.put(key, value);
    }

    public static ConcurrentHashMap<String, Object> getProperties() {
        return properties;
    }

    public static void print() {
        log.info("============================================================");
        log.info("当前环境配置信息");
        Set<String> entrySet = getProperties().keySet();
        List<String> list = new ArrayList<>(entrySet);
        Collections.sort(list);

        for (String key : list) {
            if (getProperties().containsKey(key)) {
                log.info(key + " : " + getProperties().get(key));
            }
        }
        log.info("============================================================");
    }
}
