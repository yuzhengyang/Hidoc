package com.yuzhyn.hidoc.app.manager;

import com.yuzhyn.azylee.core.datas.collections.ListTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.app.AppConf;
import com.yuzhyn.hidoc.app.application.entity.sys.SysMachine;
import com.yuzhyn.hidoc.app.application.mapper.app.AppConfMapper;
import com.yuzhyn.hidoc.app.application.mapper.sys.SysMachineMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.yuzhyn.azylee.core.datas.collections.MapTool;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormat;
import com.yuzhyn.azylee.core.datas.datetimes.DateTimeFormatPattern;
import com.yuzhyn.azylee.core.datas.datetimes.LocalDateTimeTool;
import com.yuzhyn.azylee.core.datas.ids.SnowFlake;
import com.yuzhyn.azylee.core.datas.ids.UUIDTool;
import com.yuzhyn.azylee.core.datas.numbers.LongTool;
import com.yuzhyn.azylee.core.datas.strings.StringConst;
import com.yuzhyn.azylee.core.datas.strings.StringTool;
import com.yuzhyn.azylee.core.ios.dirs.DirTool;
import com.yuzhyn.azylee.core.ios.txts.PropertyTool;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
public class AppLiveManager {
//    public static Map<String, String> codeMap = new HashMap<String, String>();

    @Autowired
    private SysMachineMapper sysMachineMapper;

    @Autowired
    private AppConfMapper appConfMapper;

    @PostConstruct
    public void init() throws Exception {
        log.info("系统启动中。。。");
        initRes();
        initProps();
        if (StringTool.ok(R.MachineId)) {

            SysMachine sysMachine = sysMachineMapper.selectById(R.MachineId);
            if (sysMachine != null) {
                // 已经预置了机器信息，直接获取并创建ID算法类
                R.DataCenterId = sysMachine.getDataCenterId();
                R.WorkerId = sysMachine.getWorkerId();
            } else {
                log.info("雪花算法未初始化，准备自动初始化，开始计算数据中心ID和机器ID");
                // 自动计算数据中心ID和机器ID（排除已有并随即）
                List<SysMachine> machineList = sysMachineMapper.selectList(null);
                if (ListTool.ok(machineList)) {
                    List<Integer> _dcIds = machineList.stream().map(SysMachine::getDataCenterId).distinct().collect(toList());
                    List<Integer> _wkIds = machineList.stream().map(SysMachine::getWorkerId).distinct().collect(toList());
                    int[] snowIdset = SnowFlake.createDataCenterIdAndWorkerId(_dcIds, _wkIds, true);
                    R.DataCenterId = snowIdset[0];
                    R.WorkerId = snowIdset[1];
                    log.info("计算完成，数据中心ID：" + R.DataCenterId + "机器ID：" + R.WorkerId);
                }

                // 创建信息并插入到数据库
                sysMachine = new SysMachine();
                sysMachine.setMachineId(R.MachineId);
                sysMachine.setIp("127.0.0.1");
                sysMachine.setMac("00:00:00:00:00:00");
                sysMachine.setDataCenterId(R.DataCenterId);
                sysMachine.setWorkerId(R.WorkerId);
                sysMachineMapper.insert(sysMachine);
            }
        }
        if (R.DataCenterId < 0 || R.WorkerId < 0) {
            String msg = StringConst.NEWLINE +
                    "中断启动：" +
                    "DataCenterId 和 WorkerId 未设置" +
                    "这将影响 SnowFlake 生成ID序号" +
                    StringConst.NEWLINE +
                    "解决方法：" +
                    "查看数据库 sys_machine 表，设置 data_center_id 和 worker_id 字段，" +
                    "取值范围：[0,31] [0,31] 包含两端";
            log.error(msg);
            throw new Exception(msg);
        }

        log.info("********** 初始化雪花ID程序 **********");
        R.SnowFlake = new SnowFlake(R.WorkerId, R.DataCenterId, 0);
        if (R.SnowFlake == null) {
            throw new Exception("SnowFlake 初始化失败！");
        }


        log.info("********** 加载全部配置选项 **********");
        List<AppConf> appConfList = appConfMapper.selectList(null);

        log.info("********** 加载通知邮箱配置 **********");
        R.Maps.emailConfig = AppConf.toMap(appConfList, "notice-email");

        log.info("********** 加载注册管理配置 **********");
        R.Maps.registerConfig = AppConf.toMap(appConfList, "register");

        log.info("********** 配置加载完毕 **********");
    }

    /**
     * 系统优雅关闭，统一触发
     */
    @PreDestroy
    public void destroy() {
        log.info("系统运行结束");
        long now = System.currentTimeMillis();
        log.info("==============================================");
        log.info("======== ShutdownManager 正在关闭..... ========");
        log.info("==============================================");

//        Sleep.s(10);

        R.Caches.CacheManager.close();

        log.info("==============================================");
        log.info("======== ShutdownManager 已经关闭 =============");
        log.info("==============================================");
        log.info("Time spent: " + (System.currentTimeMillis() - now));
    }

    /**
     * 初始化资源数据（创建目录结构，释放资源）
     */
    private void initRes() {
        log.info("********** 初始化资源数据（创建目录结构，释放资源） **********");
        log.info("** 创建文件夹 **");
        DirTool.create(R.Paths.SysFile);
        DirTool.create(R.Paths.SysFileTemp);
        DirTool.create(R.Paths.Temp);
        DirTool.create(R.Paths.ServerMan);
        DirTool.create(R.Paths.ServerManSsh);
    }

    /**
     * 初始化配置信息
     */
    private void initProps() throws Exception {
        log.info("********** 初始化配置信息 **********");
        log.info("** 配置信息：MachineInfo **");
        // 读取
        Map<String, String> machineProps = PropertyTool.read(R.Files.MachineInfo);
        R.MachineId = MapTool.get(machineProps, "machine_id", "");
        if (!StringTool.ok(R.MachineId)) R.MachineId = UUIDTool.get();
        // 写出
        machineProps.put("machine_id", R.MachineId);
        machineProps.put("desc", "设备信息");
        PropertyTool.write(R.Files.MachineInfo, machineProps);

        log.info("** 配置信息：app **");
        // 读取
        Map<String, String> appProps = PropertyTool.read(R.Files.AppInfo);
        R.RunTimes = LongTool.parseFromInts(MapTool.get(appProps, "run_times", "0"), 0);
        // 写出
        appProps.put("run_datetime", DateTimeFormat.toStr(LocalDateTime.now(), DateTimeFormatPattern.NORMAL_DATETIME));
        appProps.put("run_times", String.valueOf(R.RunTimes + 1));
        appProps.put("desc", "程序信息");
        PropertyTool.write(R.Files.AppInfo, appProps);


        log.info("** 访问信息：access **");
        // 读取
        Map<String, String> accessProps = PropertyTool.read(R.Files.AccessInfo);
        R.AccessTimes = LongTool.parse(MapTool.get(accessProps, "access_times", "0"), 0);
        R.TodayAccessTimes = LongTool.parse(MapTool.get(accessProps, "today_access_times", "0"), 0);
        LocalDateTime today = LocalDateTimeTool.parse(MapTool.getString(accessProps, "today", ""), DateTimeFormatPattern.NORMAL_DATETIME);
        // 写出
        // 如果记录的访问次数不是今天的，则需要清空次数重记并记录今天时间
        if (!LocalDateTimeTool.isSameDay(today, R.Today())) {
            R.TodayAccessTimes = 0;
            accessProps.put("today_access_times", String.valueOf(R.TodayAccessTimes));
            accessProps.put("today", DateTimeFormat.toStr(R.Today(), DateTimeFormatPattern.NORMAL_DATETIME));
            PropertyTool.write(R.Files.AccessInfo, accessProps);
        }
    }
}
