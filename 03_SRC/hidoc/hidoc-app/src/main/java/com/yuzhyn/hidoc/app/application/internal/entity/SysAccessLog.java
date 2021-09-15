package com.yuzhyn.hidoc.app.application.internal.entity;

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
    private LocalDateTime createTime;
    private String uri;
    private String method;
    private Long elapsedTime;
    private String userId;
}
