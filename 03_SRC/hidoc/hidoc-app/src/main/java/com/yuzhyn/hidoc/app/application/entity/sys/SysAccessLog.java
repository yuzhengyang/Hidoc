package com.yuzhyn.hidoc.app.application.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author inc
 */
@Data
@TableName("sys_access_log")
public class SysAccessLog {
    private String id;
    private String ip;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private String uri;
    private String method;
    private Long elapsedTime;
    private String userId;
    private String step;
    private String threadName;
    private Long beginMaxMemory;
    private Long beginTotalMemory;
    private Long beginFreeMemory;
    private Long endMaxMemory;
    private Long endTotalMemory;
    private Long endFreeMemory;
    private Long probablyUseMemory;
    private String exception;
    private String machineId;
    private String pointId;
    private String accessOrigin;
}
