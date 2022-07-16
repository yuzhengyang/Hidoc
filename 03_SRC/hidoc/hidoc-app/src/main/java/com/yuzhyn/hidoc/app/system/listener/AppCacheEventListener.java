package com.yuzhyn.hidoc.app.system.listener;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

@Slf4j
public class AppCacheEventListener implements CacheEventListener<Object, Object> {

    @Override
    public void onEvent(CacheEvent<?, ?> cacheEvent) {
        log.info("Event: " + cacheEvent.getType() + " Key: " + cacheEvent.getKey() + " old value: " + cacheEvent.getOldValue() + " new value: " + cacheEvent.getNewValue());
    }
}
