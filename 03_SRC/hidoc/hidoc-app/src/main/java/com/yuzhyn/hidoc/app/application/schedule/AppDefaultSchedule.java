package com.yuzhyn.hidoc.app.application.schedule;

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
public class AppDefaultSchedule {

    @Autowired
    SysAccessLogMapper sysAccessLogMapper;

    @Autowired
    DocAccessLogMapper docAccessLogMapper;

    @Autowired
    SysStatusLogMapper sysStatusLogMapper;

    @Autowired
    ServerManExeLogMapper serverManExeLogMapper;

    @Async
    @Scheduled(cron = "*/10 * * * * ?")
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
            }
            String file = DirTool.combine(R.Paths.ServerManSsh, dialogId + ".txt");
            TxtTool.append(file, stringBuilder.toString());
        }
    }

    @Async
    @Scheduled(cron = "0 */2 * * * ?")
    public void job() {
        // ???????????????????????????
        saveStatus();

        // ????????????????????????
        saveAccessTimes();

        // ????????????????????????
        saveSysAccessLog();

        // ????????????????????????
        saveDocAccessLog();

        // ????????????????????????
        saveSysStatusLog();

        // ??????30????????????ssh??????
        cleanSshConnection();
    }

    public void saveSysAccessLog() {
        for (int i = 0; i < 1000 * 60; i++) {
            SysAccessLog sysAccessLog = R.Queues.SysAccessLogQueue.poll();
            if (sysAccessLog != null) {
                try {
                    sysAccessLog.setMachineId(R.MachineId);
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
            DocAccessLog docAccessLog = R.Queues.DocAccessLogQueue.poll();
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
        statusProps.put("queue_size_of_sys_access_log", String.valueOf(R.Queues.SysAccessLogQueue.size()));
        PropertyTool.write(R.Files.StatusInfo, statusProps);
    }

    public void saveAccessTimes() {
        log.info("** ???????????????access **" + " AccessTimes: " + R.AccessTimes + " TodayAccessTimes: " + R.TodayAccessTimes);
        // ??????
        Map<String, String> accessProps = PropertyTool.read(R.Files.AccessInfo);
        LocalDateTime today = LocalDateTimeTool.parse(MapTool.getString(accessProps, "today", ""), DateTimeFormatPattern.NORMAL_DATETIME);
        // ??????
        accessProps.put("access_times", String.valueOf(R.AccessTimes));
        accessProps.put("today_access_times", String.valueOf(R.TodayAccessTimes));
        accessProps.put("today", DateTimeFormat.toStr(R.Today(), DateTimeFormatPattern.NORMAL_DATETIME));
        // ?????????????????????????????????????????????????????????????????????????????????????????????
        if (!LocalDateTimeTool.isSameDay(today, R.Today())) {
            R.TodayAccessTimes = 0;
            accessProps.put("today_access_times", String.valueOf(R.TodayAccessTimes));
        }
        PropertyTool.write(R.Files.AccessInfo, accessProps);
    }

    public void saveSysStatusLog() {
        try {
            SystemStatusInfo statusInfo = SystemStatusTool.getStatusInfo();
            SysStatusLog record = new SysStatusLog();
            record.setId(R.SnowFlake.nexts());
            record.setCreateTime(LocalDateTime.now());
            record.setMachineId(R.MachineId);
            record.setAvailableProcessors(statusInfo.getAvailableProcessors());
            record.setFreeSwapSpaceSize(statusInfo.getFreeSwapSpaceSize());
            record.setTotalSwapSpaceSize(statusInfo.getTotalSwapSpaceSize());
            record.setFreePhysicalMemorySize(statusInfo.getFreePhysicalMemorySize());
            record.setTotalPhysicalMemorySize(statusInfo.getTotalPhysicalMemorySize());
            record.setCommittedVirtualMemorySize(statusInfo.getCommittedVirtualMemorySize());
            record.setProcessCpuLoad(statusInfo.getProcessCpuLoad());
            record.setSystemCpuLoad(statusInfo.getSystemCpuLoad());
            record.setProcessCpuTime(statusInfo.getProcessCpuTime());
            record.setSystemLoadAverage(statusInfo.getSystemLoadAverage());
            sysStatusLogMapper.insert(record);
        } catch (Exception ex) {
        }
    }

    public void cleanSshConnection() {
        if (R.SshManager != null) {
            if (MapTool.ok(R.SshManager.getSshClientMap())) {

                log.info("cleanSshConnection: ??????????????????????????????" + R.SshManager.getSshClientMap().size());

                for (String key : R.SshManager.getSshClientMap().keySet()) {
                    SshClient client = R.SshManager.getSshClientMap().getOrDefault(key, null);
                    if (client != null) {
                        // ?????????????????????????????????????????????
                        if (!client.getSession().isConnected()) {
                            R.SshManager.getSshClientMap().remove(key);
                            log.info("cleanSshConnection: ????????????: " + key);
                        }
                        // ????????????????????????????????????
                        if (!client.getChannel().isConnected()) {
                            client.getSession().disconnect();
                            log.info("cleanSshConnection: ?????? session: " + key);
                        }
                        // ????????????????????????????????????
                        if (!client.getCreateTime().isAfter(LocalDateTime.now().minusMinutes(10))) {
                            if (client.getChannel().isConnected()) {
                                client.getChannel().disconnect();
                                log.info("cleanSshConnection: ?????? channel: " + key);
                            }
                            if (client.getSession().isConnected()) {
                                client.getSession().disconnect();
                                log.info("cleanSshConnection: ?????? session: " + key);
                            }
                            log.info("cleanSshConnection: ????????????: " + key);
                            ServerManExeLog exeLog = serverManExeLogMapper.selectById(key);
                            if (exeLog != null) {
                                exeLog.setContentTc("??????????????????");
                                serverManExeLogMapper.updateById(exeLog);
                            }
                        }
                    }
                }
                log.info("cleanSshConnection: ??????????????????????????????" + R.SshManager.getSshClientMap().size());
            } else {
                log.info("cleanSshConnection: ??????????????????????????????");
            }
        } else {
            log.info("cleanSshConnection: ?????? SshManager ??????");
        }
    }
}
