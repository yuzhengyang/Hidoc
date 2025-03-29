package com.yuzhyn.hidoc.app.application.entity.sys;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_lock")
public class SysLock {

    @TableId("key")
    private String key;

    private String val;

    private String userId;

    private LocalDateTime createTime;

    private LocalDateTime expireTime;

    private Long lockCount;

}
