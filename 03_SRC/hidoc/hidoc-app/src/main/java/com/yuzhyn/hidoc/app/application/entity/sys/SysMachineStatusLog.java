package com.yuzhyn.hidoc.app.application.entity.sys;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_machine_status_log")
public class SysMachineStatusLog {
    private String id;
    private String machineId;
    private LocalDateTime createTime;
    private Integer cpu;
    private Long ram;
    private Long disk;
    private Integer appCpu;
    private Long appRam;
    private String ssLong;
}
