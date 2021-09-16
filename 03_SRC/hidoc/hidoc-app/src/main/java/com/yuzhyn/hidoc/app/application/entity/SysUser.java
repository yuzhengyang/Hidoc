package com.yuzhyn.hidoc.app.application.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class SysUser {

    @TableId("id")
    private String id;

    @TableField("name")
    private String name;

    private String realName;

    private String avatar;

    private String email;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Boolean isFrozen;

    private String password;
}
