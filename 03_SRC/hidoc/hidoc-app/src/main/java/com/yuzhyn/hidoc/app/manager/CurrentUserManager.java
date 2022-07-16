package com.yuzhyn.hidoc.app.manager;

import com.yuzhyn.hidoc.app.application.entity.sys.SysUser;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUserFileConf;
import com.yuzhyn.hidoc.app.application.model.sys.UserInfo;
import com.yuzhyn.azylee.core.logs.Alog;

/**
 * @author inc
 */
public class CurrentUserManager {

    private static ThreadLocal<Boolean> login = ThreadLocal.withInitial(() -> false);
    private static ThreadLocal<String> token = ThreadLocal.withInitial(() -> null);
    private static ThreadLocal<SysUser> user = ThreadLocal.withInitial(() -> null);
    private static ThreadLocal<SysUser> openUser = ThreadLocal.withInitial(() -> null);
    private static ThreadLocal<SysUserFileConf> fileConfig = ThreadLocal.withInitial(() -> null);
    private static ThreadLocal<String> requestMethod = ThreadLocal.withInitial(() -> null);

    public static ThreadLocal<String> ip = ThreadLocal.withInitial(() -> "");

    public static boolean isLogin() {
        return login.get() && user != null;
    }

    public static String getToken() {
        return token.get();
    }

    public static SysUser getUser() {
        return user.get();
    }

    public static String getUserId() {
        return user.get().getId();
    }

    /**
     * 开放接口时，自动预制开放用户，没有用户信息，获取一个通用开放的用户信息
     *
     * @return
     */
    public static void createOpenUser() {
        SysUser user = new SysUser();
        user.setId("open-user");
        user.setName("OpenUser");
        user.setRealName("开放用户");
        openUser.set(user);
    }

    public static SysUser getOpenUser() {
        return openUser.get();
    }

    public static SysUserFileConf getFileConfig() {
        return fileConfig.get();
    }

    public static String getRequestMethod() {
        return requestMethod.get();
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

    public static void setRequestMethod(String method) {
        CurrentUserManager.requestMethod.set(method.toUpperCase());
    }

    public static void clearCurrentUser() {
        CurrentUserManager.login.remove();
        CurrentUserManager.token.remove();
        CurrentUserManager.user.remove();
        CurrentUserManager.fileConfig.remove();
        CurrentUserManager.ip.remove();
        CurrentUserManager.requestMethod.remove();
        CurrentUserManager.openUser.remove();
    }

    public static void cleanOpenUser() {
        CurrentUserManager.openUser.remove();
    }
}
