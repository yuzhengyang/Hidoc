package com.yuzhyn.hidoc.app.application.model.sys;

import com.yuzhyn.hidoc.app.application.entity.sys.SysUser;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserFileConf;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserLogin;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户登录信息
 * 程序启动时
 */
@Data
public class UserInfo implements Serializable {
    private String token;
    private SysUser user;
    private SysUserFileConf userFileConf;
    private String ip;
    private LocalDateTime loginTime;
    private LocalDateTime expiryTime;
//    private String teamIds;

    public Boolean getExpired() {
        if (expiryTime != null) {
            return LocalDateTime.now().isAfter(expiryTime);
        }
        return false;
    }

    public SysUserLogin getSysUserLogin() {
        SysUserLogin sysUserLogin = new SysUserLogin();
        sysUserLogin.setToken(token);
        sysUserLogin.setUserId(user.getId());
        sysUserLogin.setIp(ip);
        sysUserLogin.setLoginTime(loginTime);
        sysUserLogin.setExpiryTime(expiryTime);
        return sysUserLogin;
    }
}
