package com.yuzhyn.hidoc.app.application.customization.schedule;

import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.internal.entity.SysMachineStatusLog;
import com.yuzhyn.hidoc.app.application.internal.mapper.SysMachineStatusLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.Component;
import pers.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import pers.yuzhyn.azylee.core.datas.ids.UUIDTool;
import pers.yuzhyn.azylee.core.logs.Alog;
import pers.yuzhyn.azylee.core.threads.sleeps.Sleep;

import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Component
@EnableScheduling
@EnableAsync
public class TestSchedule {

    @Autowired
    SysMachineStatusLogMapper sysStatusLogMapper;

//    @Async
//    @Scheduled(cron = "0/1 * * * * *")
    public void logtest() {
        Thread t = Thread.currentThread();

        {
            SysMachineStatusLog log = new SysMachineStatusLog();
            log.setId(R.SnowFlake.nexts());
            log.setMachineId(R.MachineId);
            log.setCreateTime(LocalDateTime.now());
            log.setCpu(1);
            log.setRam(100L);
            log.setDisk(500L);
            log.setAppCpu(1);
            log.setAppRam(100L);
            log.setSsLong(UUIDTool.getId1024());
            int logFlag = sysStatusLogMapper.insert(log);
        }


//        for (int i = 0; i < 10; i++) {
        log.info("TestSchedule" + new Date() + " : Annotation：is show run :" + t.getId() + "," + t.getName());
        log.info(UUIDTool.getId(10));
        log.error("error log test");
//        }
    }

    //    @Async
//    @Scheduled(cron = "0/1 * * * * *")
    public void show() {
//        Thread t = Thread.currentThread();
//        Sleep.s(2);
//        System.out.println("TestSchedule" + new Date() + " : Annotation：is show run :" + t.getId() + "," + t.getName());

        long t = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            SysMachineStatusLog log = new SysMachineStatusLog();
            log.setId(R.SnowFlake.nexts());
            log.setMachineId(R.MachineId);
            log.setCreateTime(LocalDateTime.now());
            log.setCpu(1);
            log.setRam(100L);
            log.setDisk(500L);
            log.setAppCpu(1);
            log.setAppRam(100L);
            log.setSsLong(UUIDTool.getId1024());
            int logFlag = sysStatusLogMapper.insert(log);
//            Alog.w(DateTimeFormat.toStr(LocalDateTime.now()) + "    sys-status-log: " + logFlag);
//            Alog.i();
        }
        Alog.w(DateTimeFormat.toStr(LocalDateTime.now()) + "    sys-status-log 插入数据时长: " + (System.currentTimeMillis() - t));
        Alog.i();
    }

    //    @Async
//    @Scheduled(cron = "0/5 * * * * *")
    public void show2() {
        Thread t = Thread.currentThread();
        Sleep.s(5);
        System.out.println("TestSchedule" + new Date() + " : 5秒间隔 Scheduled :" + t.getId() + "," + t.getName());
    }

//    @Override
//    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//        taskRegistrar.setScheduler(taskExecutor());
//    }
//
//    @Bean(destroyMethod = "shutdown")
//    public Executor taskExecutor() {
//        return Executors.newScheduledThreadPool(100);
//    }
}
