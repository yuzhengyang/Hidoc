package com.yuzhyn.hidoc.app.application.schedule;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import com.yuzhyn.azylee.core.ios.dirs.DirTool;
import com.yuzhyn.azylee.core.ios.files.FileCharCodeTool;
import com.yuzhyn.azylee.core.ios.files.FileTool;
import com.yuzhyn.azylee.core.ios.txts.TxtTool;
import com.yuzhyn.azylee.core.threads.sleeps.Sleep;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.file.File;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursor;
import com.yuzhyn.hidoc.app.application.entity.serverman.ServerManMachine;
import com.yuzhyn.hidoc.app.application.entity.sys.SysMachine;
import com.yuzhyn.hidoc.app.application.mapper.file.FileCursorMapper;
import com.yuzhyn.hidoc.app.application.mapper.file.FileMapper;
import com.yuzhyn.hidoc.app.application.mapper.serverman.ServerManMachineMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysMachineMapper;
import com.yuzhyn.hidoc.app.application.service.file.FileService;
import com.yuzhyn.hidoc.app.application.service.sys.SysLockService;
import com.yuzhyn.hidoc.app.utils.PortTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@EnableScheduling
@EnableAsync
public class PortCheckSchedule {

    @Autowired
    private SysLockService sysLockService;

    @Autowired
    private ServerManMachineMapper serverManMachineMapper;

    @Scheduled(cron = "0 */1 * * * ?")
    public void job() {
        String key = "PortCheckSchedule";
        long expireSeconds = 300L;
        String lockKey = sysLockService.lock(key, expireSeconds, "");
        if (ObjectUtil.isEmpty(lockKey)) return;

        try {
            // 锁定成功后，开始循环检查服务器的连通性
            List<ServerManMachine> machineList = serverManMachineMapper.selectList(new LambdaQueryWrapper<ServerManMachine>()
                    .eq(ServerManMachine::getIsDelete, false));
            if (ObjectUtil.isEmpty(machineList)) return;

            for (ServerManMachine machine : machineList) {
                if (PortTool.isOpen(machine.getAddress(), machine.getPort())) {
                    machine.setPortOpen(true);
                    machine.setPortOpenTime(LocalDateTime.now());
                } else {
                    machine.setPortOpen(false);
                }
                serverManMachineMapper.update(new LambdaUpdateWrapper<ServerManMachine>()
                        .set(ServerManMachine::getPortOpen, machine.getPortOpen())
                        .set(ServerManMachine::getPortOpenTime, machine.getPortOpenTime())
                        .eq(ServerManMachine::getId, machine.getId()));
                Sleep.s(1);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
