package com.yuzhyn.hidoc.app.application.schedule;

import com.yuzhyn.azylee.core.datas.exceptions.ExceptionTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.file.File;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursor;
import com.yuzhyn.hidoc.app.application.entity.file.FileDownloadLog;
import com.yuzhyn.hidoc.app.application.mapper.file.FileCursorMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileDownloadLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
@EnableAsync
public class FileDownloadLogSchedule {

    @Autowired
    FileMapper fileMapper;

    @Autowired
    FileDownloadLogMapper fileDownloadLogMapper;

    @Autowired
    FileCursorMapper fileCursorMapper;

    @Async
    @Scheduled(cron = "0 */10 * * * ?")
    public void job() {
        // 保存文件下载日志
        saveFileDownloadLog();
    }

    public void saveFileDownloadLog() {
        for (int i = 0; i < 1000 * 60; i++) {
            FileDownloadLog logItem = R.Queues.FileDownloadLogQueue.poll();
            if (logItem != null) {
                try {
                    fileDownloadLogMapper.insert(logItem);

                    // 记录文件下载时间和下载次数
                    {
                        File file = fileMapper.selectById(logItem.getFileId());
                        if (file != null) {
                            Long count = file.getDownloadCount() != null ? file.getDownloadCount() + 1 : 1L;
                            file.setDownloadTime(logItem.getCreateTime());
                            file.setDownloadCount(count);
                            fileMapper.updateById(file);
                        }
                    }

                    // 记录文件指针下载时间和下载次数
                    {
                        FileCursor fileCursor = fileCursorMapper.selectById(logItem.getCursorId());
                        if (fileCursor != null) {
                            Long count = fileCursor.getDownloadCount() != null ? fileCursor.getDownloadCount() + 1 : 1L;
                            fileCursor.setDownloadTime(logItem.getCreateTime());
                            fileCursor.setDownloadCount(count);
                            fileCursorMapper.updateById(fileCursor);
                        }
                    }

                } catch (Exception ex) {
                    log.error(ExceptionTool.getStackTrace(ex));
                }
            } else {
                break;
            }
        }
    }
}
