package com.yuzhyn.hidoc.app.manager;

import com.yuzhyn.hidoc.app.application.entity.sys.SysUser;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserFileConf;
import com.yuzhyn.hidoc.app.application.model.UserInfo;
import com.yuzhyn.azylee.core.logs.Alog;

/**
 * @author inc
 */
public class CurrentUserManager {

    private static ThreadLocal<Boolean> login = ThreadLocal.withInitial(() -> false);
    private static ThreadLocal<String> token = ThreadLocal.withInitial(() -> null);
    private static ThreadLocal<SysUser> user = ThreadLocal.withInitial(() -> null);
    private static ThreadLocal<SysUserFileConf> fileConfig = ThreadLocal.withInitial(() -> null);

    public static boolean isLogin() {
        return login.get() && user != null;
    }

    public static String getToken() {
        return token.get();
    }

    public static SysUser getUser() {
        return user.get();
    }

    public static SysUserFileConf getFileConfig() {
        return fileConfig.get();
    }

    public static void set(UserInfo userInfo) {
        Alog.i("Thread.currentThread: " + Thread.currentThread());
        if (userInfo.getToken() != null) {
            CurrentUserManager.token.set(userInfo.getToken());
            login.set(true);
        }
        if (userInfo.getUser() != null) CurrentUserManager.user.set(userInfo.getUser());
        if (userInfo.getUserFileConf() != null) CurrentUserManager.fileConfig.set(userInfo.getUserFileConf());
    }

    public static void clearCurrentUser() {
        CurrentUserManager.login.remove();
        CurrentUserManager.token.remove();
        CurrentUserManager.user.remove();
        CurrentUserManager.fileConfig.remove();
    }
}
