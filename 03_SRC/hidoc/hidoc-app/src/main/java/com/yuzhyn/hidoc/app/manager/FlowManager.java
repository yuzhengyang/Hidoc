package com.yuzhyn.hidoc.app.manager;

import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.azylee.core.logs.Alog;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.sys.SysUser;
import com.yuzhyn.hidoc.app.application.entity.sys.SysFlowLog;
import com.yuzhyn.hidoc.app.application.entity.sys.SysFlowRule;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FlowManager {

    public static void readRules() {
        // todo: 这里需要优化成读取配置文件，或者定时读取数据库
        R.sysFlowRules = Arrays.asList(
                new SysFlowRule("/javadoc/getOriginalDocument", "all", "all", "POST", "user", 120, 60),
                new SysFlowRule("/javadoc/getSourceCode", "all", "all", "POST", "user", 120, 60)
        );
    }

    public static boolean check(String url, SysUser user) {
        try {
            return checkFlow(url, user);
        } catch (Exception e) {
            Alog.e("FlowManager: exception");
            Alog.e("FlowManager: exception is: " + e.getMessage());
            return true;
        }
    }

    private static boolean checkFlow(String url, SysUser user) {
        String key = "*:" + url;
        if (ListTool.ok(R.sysFlowRules)) {
            SysFlowRule rule = R.sysFlowRules.stream().filter(x -> x.url.equals(url)).findFirst().orElse(null);
            if (rule != null) {
                // 按照每个用户去独立计数控制
                if (rule.mode.equals("user")) {
                    if (user != null) key = user.getId() + ":" + url;
                }

                // 获取访问日志记录
                Map<Long, SysFlowLog> logs = R.Caches.SysFlowLogs.getIfPresent(key);
                if (logs == null) {
                    R.Caches.SysFlowLogs.put(key, new HashMap<>());
                    logs = R.Caches.SysFlowLogs.getIfPresent(key);
                }

                // 清理过期的访问记录
                if (logs.size() > 0) {
                    long now = System.currentTimeMillis();
                    Iterator<Map.Entry<Long, SysFlowLog>> iterator = logs.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<Long, SysFlowLog> entry = iterator.next();
                        if (now - entry.getValue().getTime() > rule.duration * 1000L) {
                            iterator.remove(); // 安全地移除条目
                        }
                    }
                }

                // 判断是否超过限制
                if (logs.size() > rule.count) {
                    // 超过限制，直接返回false
                    return false;
                }

                // 最后记录一下本次访问信息
                SysFlowLog flowLog = new SysFlowLog();
                flowLog.setId(R.SnowFlake.nexts());
                flowLog.setTime(System.currentTimeMillis());
                flowLog.setUrl(url);
                flowLog.setUser(user != null ? user.getId() : "");
                flowLog.setRole(user != null ? user.getRoles().toString() : "");
                logs.put(flowLog.getTime(), flowLog);
            }
        }
        return true;
    }
}
