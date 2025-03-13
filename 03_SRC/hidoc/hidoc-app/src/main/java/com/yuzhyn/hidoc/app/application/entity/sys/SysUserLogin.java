package com.yuzhyn.hidoc.app.application.entity.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yuzhyn.hidoc.app.application.model.sys.UserInfo;
import com.yuzhyn.hidoc.app.system.ibatis.handler.JsonbTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "sys_user_login", autoResultMap = true)
public class SysUserLogin {

    @TableId("token")
    private String token;

    private String userId;

    private String ip;

    private LocalDateTime loginTime;

    private LocalDateTime expiryTime;

    @TableField(typeHandler = JsonbTypeHandler.class)
    private UserInfo userInfo;
}
