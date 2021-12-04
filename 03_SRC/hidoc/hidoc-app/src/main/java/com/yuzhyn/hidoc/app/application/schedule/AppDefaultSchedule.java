package com.yuzhyn.hidoc.app.application.schedule;

import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.doc.DocAccessLog;
import com.yuzhyn.hidoc.app.application.entity.sys.SysAccessLog;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocAccessLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysAccessLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import com.yuzhyn.azylee.core.datas.datetimes.LocalDateTimeTool;
import com.yuzhyn.azylee.core.datas.exceptions.ExceptionTool;
import com.yuzhyn.azylee.core.ios.txts.PropertyTool;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Component
@EnableScheduling
@EnableAsync
public class AppDefaultSchedule {

    @Autowired
    SysAccessLogMapper sysAccessLogMapper;

    @Autowired
    DocAccessLogMapper docAccessLogMapper;

    @Async
    @Scheduled(cron = "0 */2 * * * ?")
    public void job() {
        // 保存当前各状态信息
        saveStatus();

        // 定时保存访问次数
        saveAccessTimes();

        // 保存访问接口日志
        saveSysAccessLog();
        // 保存文章阅读日志
        saveDocAccessLog();
    }

    public void saveSysAccessLog() {
        for (int i = 0; i < 1000 * 60; i++) {
            SysAccessLog sysAccessLog = R.Queue.SysAccessLogQuene.poll();
            if (sysAccessLog != null) {
                try {
                    sysAccessLogMapper.insert(sysAccessLog);
                } catch (Exception ex) {
                    log.error(ExceptionTool.getStackTrace(ex));
                }
            } else {
                break;
            }
        }
    }

    public void saveDocAccessLog() {
        for (int i = 0; i < 1000 * 60; i++) {
            DocAccessLog docAccessLog = R.Queue.DocAccessLogQuene.poll();
            if (docAccessLog != null) {
                try {
                    docAccessLogMapper.insert(docAccessLog);
                } catch (Exception ex) {
                    log.error(ExceptionTool.getStackTrace(ex));
                }
            } else {
                break;
            }
        }
    }

    public void saveStatus() {
        Map<String, String> statusProps = PropertyTool.read(R.Files.StatusInfo);
        statusProps.put("update_time", DateTimeFormat.toStr(R.Today(), DateTimeFormatPattern.NORMAL_DATETIME));
        statusProps.put("queue_size_of_sys_access_log", String.valueOf(R.Queue.SysAccessLogQuene.size()));
        PropertyTool.write(R.Files.StatusInfo, statusProps);
    }

    public void saveAccessTimes() {
        log.info("** 访问信息：access **" + " AccessTimes: " + R.AccessTimes + " TodayAccessTimes: " + R.TodayAccessTimes);
        // 读取
        Map<String, String> accessProps = PropertyTool.read(R.Files.AccessInfo);
        LocalDateTime today = LocalDateTimeTool.parse(MapTool.getString(accessProps, "today", ""), DateTimeFormatPattern.NORMAL_DATETIME);
        // 写出
        accessProps.put("access_times", String.valueOf(R.AccessTimes));
        accessProps.put("today_access_times", String.valueOf(R.TodayAccessTimes));
        accessProps.put("today", DateTimeFormat.toStr(R.Today(), DateTimeFormatPattern.NORMAL_DATETIME));
        // 如果记录的访问次数不是今天的，则需要清空次数重记并记录今天时间
        if (!LocalDateTimeTool.isSameDay(today, R.Today())) {
            R.TodayAccessTimes = 0;
            accessProps.put("today_access_times", String.valueOf(R.TodayAccessTimes));
        }
        PropertyTool.write(R.Files.AccessInfo, accessProps);
    }
}
