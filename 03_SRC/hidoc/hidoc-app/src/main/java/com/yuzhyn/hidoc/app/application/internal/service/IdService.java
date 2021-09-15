package com.yuzhyn.hidoc.app.application.internal.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pers.yuzhyn.azylee.core.datas.ids.UUIDTool;

import javax.cache.annotation.CacheResult;

@Slf4j
@Service
public class IdService {
    @CacheResult(cacheName = "ids")
    public String getId() {
        log.info("未从缓存读取");
        return UUIDTool.get();
    }
}
