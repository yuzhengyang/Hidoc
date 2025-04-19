package com.yuzhyn.hidoc.app.application.entity.sys;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yuzhyn.hidoc.app.system.ibatis.handler.JsonbTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "sys_user", autoResultMap = true)
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

    private Boolean isSleep;

    private String password;

    private LocalDateTime onlineTime;

    private LocalDateTime loginTime;

    private Integer vipLevel;

    private String totpSeed;

    private LocalDateTime totpCreateTime;

    @TableField(typeHandler = JsonbTypeHandler.class)
    private JSONArray roles;
}
