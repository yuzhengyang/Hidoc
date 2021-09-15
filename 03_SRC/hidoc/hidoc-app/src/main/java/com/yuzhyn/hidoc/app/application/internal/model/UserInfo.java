package com.yuzhyn.hidoc.app.application.internal.model;

import com.yuzhyn.hidoc.app.application.internal.entity.SysUser;
import com.yuzhyn.hidoc.app.application.internal.entity.SysUserFileConf;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfo implements Serializable {
    private String token;
    private SysUser user;
    private SysUserFileConf userFileConf;
}
