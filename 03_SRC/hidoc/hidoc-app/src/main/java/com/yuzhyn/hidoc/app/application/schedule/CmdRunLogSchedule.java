package com.yuzhyn.hidoc.app.application.schedule;

import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import com.yuzhyn.azylee.core.datas.datetimes.LocalDateTimeTool;
import com.yuzhyn.azylee.core.datas.exceptions.ExceptionTool;
import com.yuzhyn.azylee.core.datas.strings.StringConst;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.azylee.core.ios.dirs.DirTool;
import com.yuzhyn.azylee.core.ios.txts.PropertyTool;
import com.yuzhyn.azylee.core.ios.txts.TxtTool;
import com.yuzhyn.azylee.core.systems.bases.SystemStatusTool;
import com.yuzhyn.azylee.core.systems.models.SystemStatusInfo;
import com.yuzhyn.azylee.core.threads.sleeps.Sleep;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.doc.DocAccessLog;
import com.yuzhyn.hidoc.app.application.entity.serverman.ServerManExeLog;
import com.yuzhyn.hidoc.app.application.entity.sys.SysAccessLog;
import com.yuzhyn.hidoc.app.application.entity.sys.SysStatusLog;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocAccessLogMapper;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@EnableScheduling
@EnableAsync
public class CmdRunLogSchedule {

    @Autowired
    SysAccessLogMapper sysAccessLogMapper;

    @Autowired
    DocAccessLogMapper docAccessLogMapper;

    @Autowired
    SysStatusLogMapper sysStatusLogMapper;

    @Autowired
    ServerManExeLogMapper serverManExeLogMapper;


//    @Async // 是否等待上一线程执行完毕再执行，使用是不等待，直接创建执行，会产生并行执行

    /**
     * 每隔2秒，刷出SH执行日志到文件中保存
     */
    @Scheduled(cron = "*/2 * * * * ?")
    public void job1() {
        Map<String, List<String>> groupMap = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            CmdRunLog cmdRunLog = R.Queues.CmdRunLogQueue.poll();
            if (cmdRunLog == null) break;

            if (!groupMap.containsKey(cmdRunLog.getDialogId())) {
                groupMap.put(cmdRunLog.getDialogId(), new ArrayList<>());
            }

            groupMap.get(cmdRunLog.getDialogId()).add(new String(cmdRunLog.getTextBytes()));
        }

        for (String dialogId : groupMap.keySet()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String text : groupMap.get(dialogId)) {
                stringBuilder.append(text);
                stringBuilder.append(StringConst.NEWLINE);

                // 如果读取到了特殊识别标记，则根据标记操作（关闭链接）
                // [[##hidoc->serverman.run::end.succ//blablablabla]]
                if (StringTool.ok(text) && text.contains("[[##hidoc->serverman.run::")) {

                    R.SshManager.close(dialogId);
                    ServerManExeLog exeLog = serverManExeLogMapper.selectById(dialogId);

                    if (exeLog != null) {
                        if (text.contains("[[##hidoc->serverman.run::end.manager")) {
                            exeLog.setResultTa("ssh执行完成");
                        }
                        if (text.contains("[[##hidoc->serverman.run::end.succ")) {
                            exeLog.setResultTa("执行成功");
                        }
                        if (text.contains("[[##hidoc->serverman.run::end.fail")) {
                            exeLog.setResultTa("执行失败");
                        }
                        serverManExeLogMapper.updateById(exeLog);
                    }
                }
            }
            String file = DirTool.combine(R.Paths.ServerManSsh, dialogId + ".txt");
            TxtTool.append(file, stringBuilder.toString());
        }
    }
}
