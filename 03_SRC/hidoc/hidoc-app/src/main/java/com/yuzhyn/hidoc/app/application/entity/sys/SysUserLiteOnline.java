package com.yuzhyn.hidoc.app.application.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class SysUserLiteOnline {

    @TableId("id")
    private String id;

    private LocalDateTime onlineTime;
}
