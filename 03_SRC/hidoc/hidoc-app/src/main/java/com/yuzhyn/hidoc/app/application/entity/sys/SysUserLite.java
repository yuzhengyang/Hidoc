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
public class SysUserLite {

    @TableId("id")
    private String id;

    @TableField("name")
    private String name;

    private String realName;

    private String avatar;

    private String email;

    private Boolean isFrozen;

    private LocalDateTime createTime;

    private LocalDateTime onlineTime;

    @TableField(exist = false)
    private String memberDesc;

    @TableField(exist = false)
    private Boolean isOnline;

    private Integer vipLevel;

    @TableField(typeHandler = JsonbTypeHandler.class)
    private JSONArray roles;
}
