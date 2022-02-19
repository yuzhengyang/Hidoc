package com.yuzhyn.hidoc.app.manager;

import com.yuzhyn.hidoc.app.application.entity.sys.SysAccessLog;

public class CurrentAccessManager {

    private static ThreadLocal<Boolean> Access = ThreadLocal.withInitial(() -> false);
    private static ThreadLocal<SysAccessLog> webAccessLog = ThreadLocal.withInitial(() -> null);

    public static boolean isAccess() {
        boolean ac = Access.get();
        SysAccessLog lg = webAccessLog.get();
        return ac && lg != null;
    }

    public static SysAccessLog getWebAccessLog() {
        if (isAccess()) {
            return webAccessLog.get();
        }
        return null;
    }

    public static void set(SysAccessLog sysAccessLog) {
        if (sysAccessLog != null) {
            CurrentAccessManager.webAccessLog.set(sysAccessLog);
            CurrentAccessManager.Access.set(true);
        }
    }

    public static void clear() {
        CurrentAccessManager.webAccessLog.remove();
    }
}