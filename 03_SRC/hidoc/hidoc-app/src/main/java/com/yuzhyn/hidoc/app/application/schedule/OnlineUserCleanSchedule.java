package com.yuzhyn.hidoc.app.application.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@EnableAsync
public class OnlineUserCleanSchedule {

    @Async
    @Scheduled(cron = "0 */30 * * * ?")
    public void job() {
        // 清理已经过期的登录用户信息
//        for (Iterator<Cache.Entry<String, UserInfo>> i = R.Caches.UserInfo.iterator(); i.hasNext(); ) {
//            Cache.Entry<String, UserInfo> item = i.next();
//
//        }

        // 解锁已经下线的用户锁定的文档

    }
}
