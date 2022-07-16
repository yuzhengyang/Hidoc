package com.yuzhyn.hidoc.app.application.entity.sys;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_status_log")
public class SysStatusLog {

    @TableId("id")
    private String id;
    private LocalDateTime createTime;
    private String machineId;
    private Integer availableProcessors;
    private Long freeSwapSpaceSize;
    private Long totalSwapSpaceSize;
    private Long freePhysicalMemorySize;
    private Long totalPhysicalMemorySize;
    private Long committedVirtualMemorySize;
    private Double processCpuLoad;
    private Double systemCpuLoad;
    private Long processCpuTime;
    private Double systemLoadAverage;
}
