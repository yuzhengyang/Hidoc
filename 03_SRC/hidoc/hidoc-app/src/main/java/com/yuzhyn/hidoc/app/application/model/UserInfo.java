package com.yuzhyn.hidoc.app.application.model;

import com.yuzhyn.hidoc.app.application.entity.SysUser;
import com.yuzhyn.hidoc.app.application.entity.SysUserFileConf;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfo implements Serializable {
    private String token;
    private SysUser user;
    private SysUserFileConf userFileConf;
}
