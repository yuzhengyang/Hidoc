package com.yuzhyn.hidoc.app.application.model.sys;

import com.yuzhyn.hidoc.app.application.entity.sys.SysUser;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserFileConf;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserInfo implements Serializable {
    private String token;
    private SysUser user;
    private SysUserFileConf userFileConf;
    private String ip;
    private Boolean isCurrent;
    private LocalDateTime loginTime;
    private LocalDateTime expiryTime;

    public Boolean getExpired() {
        if (expiryTime != null) {
            return LocalDateTime.now().isAfter(expiryTime);
        }
        return false;
    }
}
