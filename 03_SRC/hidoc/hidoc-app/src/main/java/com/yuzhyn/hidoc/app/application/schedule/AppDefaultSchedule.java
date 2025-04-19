package com.yuzhyn.hidoc.app.application.schedule;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import com.yuzhyn.azylee.core.datas.datetimes.LocalDateTimeTool;
import com.yuzhyn.azylee.core.datas.exceptions.ExceptionTool;
import com.yuzhyn.azylee.core.datas.ids.UUIDTool;
import com.yuzhyn.azylee.core.datas.numbers.LongTool;
import com.yuzhyn.azylee.core.datas.strings.StringConst;
import com.yuzhyn.azylee.core.ios.dirs.DirTool;
import com.yuzhyn.azylee.core.ios.txts.PropertyTool;
import com.yuzhyn.azylee.core.ios.txts.TxtTool;
import com.yuzhyn.azylee.core.systems.bases.SystemStatusTool;
import com.yuzhyn.azylee.core.systems.models.SystemStatusInfo;
import com.yuzhyn.azylee.core.threads.sleeps.Sleep;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.doc.DocAccessLog;
import com.yuzhyn.hidoc.app.application.entity.file.File;
import com.yuzhyn.hidoc.app.application.entity.file.FileDownloadLog;
import com.yuzhyn.hidoc.app.application.entity.javadoc.JavaDocQueryLog;
import com.yuzhyn.hidoc.app.application.entity.serverman.ServerManExeLog;
import com.yuzhyn.hidoc.app.application.entity.sys.SysAccessLog;
import com.yuzhyn.hidoc.app.application.entity.sys.SysMachine;
import com.yuzhyn.hidoc.app.application.entity.sys.SysStatusLog;
import com.yuzhyn.hidoc.app.application.mapper.doc.DocAccessLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileDownloadLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileMapper;
import com.yuzhyn.hidoc.app.application.mapper.javadoc.JavaDocQueryLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.serverman.ServerManExeLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysAccessLogMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysMachineMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysStatusLogMapper;
import com.yuzhyn.hidoc.app.application.model.serverman.CmdRunLog;
import com.yuzhyn.hidoc.app.utils.EsTool;
import com.yuzhyn.hidoc.app.utils.TimeZoneTool;
import com.yuzhyn.hidoc.app.utils.ssh.SshClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
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

    @Autowired
    JavaDocQueryLogMapper javaDocQueryLogMapper;

    @Autowired
    SysMachineMapper sysMachineMapper;

    @Value("${app-custom.env:}")
    private String CeKey;


    @Async
    @Scheduled(cron = "0 */1 * * * ?")
    public void job() {
//        if (CeKey != null) {
//            System.out.println("CeKey: " + CeKey);
//        }

        // 保存当前各状态信息
        saveStatus();

        // 定时保存访问次数
        saveAccessTimes();

        // 保存访问接口日志
        saveSysAccessLog();

        // 保存查询javadoc日志
        saveJavaDocQueryLog();

        // 保存文章阅读日志
        saveDocAccessLog();

        // 保存系统状态日志
        saveSysStatusLog();

        // 清理30分钟前的ssh连接
        cleanSshConnection();

        // 定时刷新密钥
        resetSecretKey();

        // 定时获取其他的服务信息
        getOtherSysMachine();

    }

    public void getOtherSysMachine() {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime twoMinutesAgo = now.minusMinutes(2);
            String machineId = R.MachineId;
            List<SysMachine> list = sysMachineMapper.selectList(new LambdaQueryWrapper<SysMachine>()
                    .ne(SysMachine::getMachineId, machineId)
                    .gt(SysMachine::getUpdateTime, twoMinutesAgo)
                    .isNotNull(SysMachine::getApiUrl)
                    .ne(SysMachine::getApiUrl, "").orderByAsc(SysMachine::getSecretKey));
            if (ObjectUtil.isNotEmpty(list)) {
                for (SysMachine sysMachine : list) {
                    try {
                        R.Caches.sysMachines.put(sysMachine.getMachineId(), sysMachine);
                    } catch (Exception ex) {
                        log.error(ExceptionUtil.stacktraceToString(ex, 10));
                    }
                }
            }
        } catch (Exception ex) {
        }
    }

    public void resetSecretKey() {
        try {
            String machineId = R.MachineId;
            String uuid = UUIDTool.get();
            if (ObjectUtil.isNotEmpty(machineId) && ObjectUtil.isNotEmpty(uuid)) {
                UpdateWrapper<SysMachine> updateWrapper = new UpdateWrapper<SysMachine>()
                        .set("secret_key", uuid)
                        .set("update_time", LocalDateTime.now())
                        .eq("machine_id", machineId);
                sysMachineMapper.update(updateWrapper);
                R.Caches.ServerSecretKey.put(uuid, LocalDateTime.now());
            }
        } catch (Exception ex) {
        }
    }

    public void saveSysAccessLog() {
        for (int i = 0; i < 1000 * 60; i++) {
            SysAccessLog sysAccessLog = R.Queues.SysAccessLogQueue.poll();
            if (sysAccessLog != null) {
                try {
                    sysAccessLog.setMachineId(R.MachineId);
                    sysAccessLogMapper.insert(sysAccessLog);

                    if (R.EsTool != null) R.EsTool.createDocument("sys.access", sysAccessLog.getId(), sysAccessLog);
                } catch (Exception ex) {
                    log.error(ExceptionTool.getStackTrace(ex));
                }
            } else {
                break;
            }
        }
    }

    public void saveJavaDocQueryLog() {
        for (int i = 0; i < 1000 * 60; i++) {
            JavaDocQueryLog javaDocQueryLog = R.Queues.JavaDocQueryLogQueue.poll();
            if (javaDocQueryLog != null) {
                try {
                    javaDocQueryLogMapper.insert(javaDocQueryLog);

                    if (R.EsTool != null) R.EsTool.createDocument("javadoc.query", javaDocQueryLog.getId(), javaDocQueryLog);
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

            if (R.EsTool != null) R.EsTool.createDocument("sys.status", record.getId(), record);
        } catch (Exception ex) {
        }
    }

    public void cleanSshConnection() {
        if (R.SshManager != null) {
            if (MapTool.ok(R.SshManager.getSshClientMap())) {

                log.info("cleanSshConnection: 清理前：现存连接数：" + R.SshManager.getSshClientMap().size());

                for (String key : R.SshManager.getSshClientMap().keySet()) {
                    SshClient client = R.SshManager.getSshClientMap().getOrDefault(key, null);
                    if (client != null) {
                        // 如果连接已关闭，则移除连接信息
                        if (!client.getSession().isConnected()) {
                            R.SshManager.getSshClientMap().remove(key);
                            log.info("cleanSshConnection: 移除连接: " + key);
                        }
                        // 如果通道关闭，则关闭会话
                        if (!client.getChannel().isConnected()) {
                            client.getSession().disconnect();
                            log.info("cleanSshConnection: 关闭 session: " + key);
                        }
                        // 如果连接超时，则关闭连接
                        if (!client.getCreateTime().isAfter(LocalDateTime.now().minusMinutes(10))) {
                            if (client.getChannel().isConnected()) {
                                client.getChannel().disconnect();
                                log.info("cleanSshConnection: 关闭 channel: " + key);
                            }
                            if (client.getSession().isConnected()) {
                                client.getSession().disconnect();
                                log.info("cleanSshConnection: 关闭 session: " + key);
                            }
                            log.info("cleanSshConnection: 关闭连接: " + key);
                            ServerManExeLog exeLog = serverManExeLogMapper.selectById(key);
                            if (exeLog != null) {
                                exeLog.setContentTc("超时关闭连接");
                                serverManExeLogMapper.updateById(exeLog);
                            }
                        }
                    }
                }
                log.info("cleanSshConnection: 清理后：现存连接数：" + R.SshManager.getSshClientMap().size());
            } else {
                log.info("cleanSshConnection: 没有要处理的连接信息");
            }
        } else {
            log.info("cleanSshConnection: 异常 SshManager 为空");
        }
    }
}
