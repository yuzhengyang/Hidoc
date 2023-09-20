package com.yuzhyn.hidoc.app.application.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import com.yuzhyn.azylee.core.ios.dirs.DirTool;
import com.yuzhyn.azylee.core.ios.files.FileTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.file.File;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursor;
import com.yuzhyn.hidoc.app.application.mapper.file.FileCursorMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysUserFileConfMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@EnableScheduling
@EnableAsync
public class FileTempCleanSchedule {

    @Autowired
    FileMapper fileMapper;

    @Autowired
    FileCursorMapper fileCursorMapper;

    @Autowired
    SysUserFileConfMapper sysUserFileConfMapper;

    /**
     * 定时删除前天的临时文件夹 now-2
     */
    @Scheduled(cron = "0 */30 * * * ?")
    public void job() {
        log.info("定时清理temp目录任务，启动");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime befyest = now.minusDays(2);
        for (int i = 0; i < 30; i++) {
            try {
                String tempPath = DirTool.combine(R.Paths.Temp, DateTimeFormat.toStr(befyest.minusDays(i), DateTimeFormatPattern.SHORT_DATE));
                log.info("定时清理temp目录任务，目录：" + tempPath);
                DirTool.delete(tempPath);
            } catch (Exception ex) {
            }
        }
        log.info("定时清理temp目录任务，结束");
    }
}
