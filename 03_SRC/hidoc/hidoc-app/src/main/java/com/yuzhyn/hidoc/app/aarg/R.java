package com.yuzhyn.hidoc.app.aarg;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.yuzhyn.azylee.core.datas.ids.SnowFlake;
import com.yuzhyn.azylee.core.ios.dirs.DirTool;
import com.yuzhyn.azylee.core.systems.bases.SystemPropertyTool;
import com.yuzhyn.azylee.core.systems.bases.SystemType;
import com.yuzhyn.azylee.core.systems.bases.SystemTypeTool;
import com.yuzhyn.hidoc.app.application.entity.doc.DocAccessLog;
import com.yuzhyn.hidoc.app.application.entity.file.File;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursor;
import com.yuzhyn.hidoc.app.application.entity.file.FileDownloadLog;
import com.yuzhyn.hidoc.app.application.entity.javadoc.JavaDocQueryLog;
import com.yuzhyn.hidoc.app.application.entity.sys.SysAccessLog;
import com.yuzhyn.hidoc.app.application.entity.sys.SysFlowLog;
import com.yuzhyn.hidoc.app.application.entity.sys.SysFlowRule;
import com.yuzhyn.hidoc.app.application.entity.sys.SysMachine;
import com.yuzhyn.hidoc.app.application.model.serverman.CmdRunLog;
import com.yuzhyn.hidoc.app.utils.EsTool;
import com.yuzhyn.hidoc.app.utils.ssh.SshManager;
import reactor.util.function.Tuple3;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public final class R {

    public static String AppName = "Hidoc";
    public static String AppNameCn = "Hidoc文档";
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
    public static SystemType SystemType = SystemTypeTool.getOSname();
    public static SshManager SshManager = new SshManager();
    public static List<SysFlowRule> sysFlowRules = new ArrayList();

    public static LocalDateTime Today() {
        return LocalDateTime.now();
    }

    public static EsTool EsTool = null;

    public static String OtherSysMachineId = "";

    // 原子计数器
    public static class Atomic {
        public static AtomicLong ServerManOutput = new AtomicLong(0);
    }

    public static class Caches {
        //        public static Cache<String, UserInfo> UserInfo = CacheBuilder.newBuilder().build();
        public static Cache<String, FileCursor> SysFileCursor = CacheBuilder.newBuilder().build();
        public static Cache<String, File> SysFile = CacheBuilder.newBuilder().build();
        /**
         * key: 用户+url、角色+url
         * value: 访问记录列表
         */
        public static Cache<String, Map<Long, SysFlowLog>> SysFlowLogs = CacheBuilder.newBuilder().build();
        public static Cache<String, LocalDateTime> ServerSecretKey = CacheBuilder.newBuilder().expireAfterAccess(3, TimeUnit.MINUTES).build();
        public static Cache<String, SysMachine> sysMachines = CacheBuilder.newBuilder().expireAfterAccess(3, TimeUnit.MINUTES).build();
        public static Cache<String, File> NotExistFile =CacheBuilder.newBuilder().build();
    }

    public static class Queues {
        public static ConcurrentLinkedQueue<SysAccessLog> SysAccessLogQueue = new ConcurrentLinkedQueue<>();
        public static ConcurrentLinkedQueue<DocAccessLog> DocAccessLogQueue = new ConcurrentLinkedQueue<>();
        public static ConcurrentLinkedQueue<CmdRunLog> CmdRunLogQueue = new ConcurrentLinkedQueue<>();
        public static ConcurrentLinkedQueue<FileDownloadLog> FileDownloadLogQueue = new ConcurrentLinkedQueue<>();
        public static ConcurrentLinkedQueue<JavaDocQueryLog> JavaDocQueryLogQueue = new ConcurrentLinkedQueue<>();
    }

    public static class Maps {
        /**
         * 验证码
         * 邮箱：{唯一ID，验证码，生成时间}
         */
        public static ConcurrentHashMap<String, Tuple3<String, String, Long>> AuthCode = new ConcurrentHashMap<>();

        public static Map<String, String> emailConfig = new HashMap<>();
        public static Map<String, String> registerConfig = new HashMap<>();
        public static Map<String, String> elasticConfig = new HashMap<>();
        public static Map<String, String> functionConfig = new HashMap<>();
    }

    public static class Paths {
        public static final String App = SystemPropertyTool.userDir();
        public static final String AppData = DirTool.combine(App, "hidoc_data");
        public static final String Properties = DirTool.combine(AppData, "properties");
        public static final String SysFile = DirTool.combine(AppData, "sysfile");
        public static final String ServerMan = DirTool.combine(AppData, "serverman");
        public static final String ServerManSsh = DirTool.combine(ServerMan, "ssh");
        public static final String Temp = DirTool.combine(AppData, "temp");
        public static final String SysFileTemp = DirTool.combine(SysFile, "temp");
    }

    public static class Files {
        public static final String App = "x";
        public static final String AppInfo = DirTool.combine(Paths.Properties, "app_info.properties");
        public static final String MachineInfo = DirTool.combine(Paths.Properties, "machine_info.properties");
        public static final String AccessInfo = DirTool.combine(Paths.Properties, "access_info.properties");
        public static final String StatusInfo = DirTool.combine(Paths.Properties, "status_info.properties");
        public static final String FilesInfo = DirTool.combine(Paths.Properties, "files_info.txt");
    }

    public static class Buckets {

        /**
         * 文档素材文件夹名称
         */
        public static final String HidocFile = ".hidoc";
        /**
         * 头像文件夹
         */
        public static final String Avatar = ".avatar";
        public static final String Share = ".share";
    }
}
