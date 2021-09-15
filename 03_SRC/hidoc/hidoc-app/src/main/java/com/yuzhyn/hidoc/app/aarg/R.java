package com.yuzhyn.hidoc.app.aarg;

import com.yuzhyn.hidoc.app.application.internal.entity.SysAccessLog;
import com.yuzhyn.hidoc.app.application.internal.entity.SysFile;
import com.yuzhyn.hidoc.app.application.internal.entity.SysFileCursor;
import com.yuzhyn.hidoc.app.application.internal.model.UserInfo;
import org.ehcache.CacheManager;
import pers.yuzhyn.azylee.core.datas.ids.SnowFlake;
import pers.yuzhyn.azylee.core.ios.dirs.DirTool;
import pers.yuzhyn.azylee.core.systems.commons.SystemPropertyTool;
import reactor.util.function.Tuple2;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class R {

    public static final String AppName = "Hidoc";
    public static final String AppNameCH = "Hidoc文档";
    public static final String AppNameCHShort = "文档";
    public static final String AppId = "";
    public static final LocalDateTime StartTime = LocalDateTime.now();
    public static String MachineId = "";
    public static int WorkerId = -1;
    public static int DataCenterId = -1;
    public static long RunTimes = 0;
    public static SnowFlake SnowFlake = null;
    public static int MinPasswordLength = 6;
    public static long AccessTimes = 0;
    public static long TodayAccessTimes = 0;

    public static LocalDateTime Today() {
        return LocalDateTime.now();
    }

    public static class Cache {
        public static CacheManager CacheManager;
        public static org.ehcache.Cache<String, UserInfo> UserInfo;
        public static org.ehcache.Cache<String, SysFileCursor> SysFileCursor;
        public static org.ehcache.Cache<String, SysFile> SysFile;
    }

    public static class Queue{
        public static ConcurrentLinkedQueue<SysAccessLog> SysAccessLogQuene = new ConcurrentLinkedQueue<>();
    }

    public static class Paths {
        public static final String App = SystemPropertyTool.userDir();
        public static final String AppData = DirTool.combine(App, "hidoc_data");
        public static final String Properties = DirTool.combine(AppData, "properties");
        public static final String SysFile = DirTool.combine(AppData, "sysfile");
        public static final String SysFileTemp = DirTool.combine(SysFile, "temp");
    }

    public static class Files {
        public static final String App = "x";
        public static final String AppInfo = DirTool.combine(Paths.Properties, "app_info.properties");
        public static final String MachineInfo = DirTool.combine(Paths.Properties, "machine_info.properties");
        public static final String AccessInfo = DirTool.combine(Paths.Properties, "access_info.properties");
        public static final String StatusInfo = DirTool.combine(Paths.Properties, "status_info.properties");
    }
}
