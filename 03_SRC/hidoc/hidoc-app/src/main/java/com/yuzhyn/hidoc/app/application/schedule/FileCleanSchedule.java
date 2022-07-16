package com.yuzhyn.hidoc.app.application.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import com.yuzhyn.azylee.core.datas.datetimes.LocalDateTimeTool;
import com.yuzhyn.azylee.core.datas.exceptions.ExceptionTool;
import com.yuzhyn.azylee.core.datas.strings.StringConst;
import com.yuzhyn.azylee.core.ios.dirs.DirTool;
import com.yuzhyn.azylee.core.ios.txts.PropertyTool;
import com.yuzhyn.azylee.core.ios.txts.TxtTool;
import com.yuzhyn.azylee.core.systems.bases.SystemStatusTool;
import com.yuzhyn.azylee.core.systems.models.SystemStatusInfo;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.doc.DocAccessLog;
import com.yuzhyn.hidoc.app.application.entity.file.File;
import com.yuzhyn.hidoc.app.application.entity.serverman.ServerManExeLog;
import com.yuzhyn.hidoc.app.application.entity.sys.SysAccessLog;
import com.yuzhyn.hidoc.app.application.entity.sys.SysStatusLog;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocAccessLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileMapper;
import com.yuzhyn.hidoc.app.application.mapper.serverman.ServerManExeLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysAccessLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysStatusLogMapper;
import com.yuzhyn.hidoc.app.application.model.serverman.CmdRunLog;
import com.yuzhyn.hidoc.app.utils.ssh.SshClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Component
@EnableScheduling
@EnableAsync
public class FileCleanSchedule {

    @Autowired
    FileMapper fileMapper;

    @Async
    @Scheduled(cron = "0 */30 * * * ?")
    public void job() {
        // 查询需要释放的文件列表
        List<File> releaseFileList = fileMapper.selectList(new LambdaQueryWrapper<File>().in(File::getReleaseStatus, Arrays.asList(1, 2, 3, 4)));

        // 查询指针全部无效的文件，标记释放状态

    }
}
