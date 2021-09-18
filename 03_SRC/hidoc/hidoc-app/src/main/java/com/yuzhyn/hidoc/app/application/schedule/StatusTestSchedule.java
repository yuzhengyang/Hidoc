package com.yuzhyn.hidoc.app.application.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import com.yuzhyn.azylee.core.threads.sleeps.Sleep;

import java.util.Date;
import java.util.Map;

//@Component
//@EnableScheduling
public class StatusTestSchedule {

    @Scheduled(cron = "0/1 * * * * *")
    public void show() {
        Map<Thread, StackTraceElement[]> maps = Thread.getAllStackTraces();
        if (maps != null) {
        }
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void show2() {
        Thread t = Thread.currentThread();
        Sleep.s(5);
        System.out.println("SystemStatusSchedule" + new Date() + " : 5秒间隔 Scheduled :" + t.getId() + "," + t.getName());
    }
}
