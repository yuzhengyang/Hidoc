package com.yuzhyn.hidoc.app;

import com.yuzhyn.azylee.core.systems.bases.SystemPropertyTool;
import com.yuzhyn.azylee.core.systems.bases.SystemStatusTool;
import com.yuzhyn.azylee.core.systems.bases.SystemTypeTool;
import com.yuzhyn.hidoc.app.manager.CurrentEnvironmentManager;
import com.yuzhyn.hidoc.app.utils.TaobaoTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchClientAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@Slf4j
@EnableCaching
@SpringBootApplication(exclude = {ElasticsearchClientAutoConfiguration.class})
public class HidocAppApplication {


    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();

        System.out.println("~");
        SpringApplication app = new SpringApplication(HidocAppApplication.class);
        app.run(args);

        // 获取当前环境和配置信息
        CurrentEnvironmentManager.add("log.level", (log.isTraceEnabled() ? "trace," : "") +
                (log.isDebugEnabled() ? "debug," : "") + (log.isInfoEnabled() ? "info," : "") +
                (log.isWarnEnabled() ? "warn," : "") + (log.isErrorEnabled() ? "error" : ""));
        CurrentEnvironmentManager.add("system", SystemTypeTool.getOSname());
        CurrentEnvironmentManager.add("user.dir", SystemPropertyTool.userDir());
        log.trace("log.trace");
        log.debug("log.debug");
        log.info("log.info");
        log.warn("log.warn");
        log.error("log.error");

        log.info("/");
        log.info("============================================================");
        log.info("============================================================");
        log.info("");
        log.info("***HidocDeployer***::CODE::LaunchedSuccessfully");
        log.info("hidoc 服务启动成功");
        log.info("");
        log.info(SystemStatusTool.getStatusInfo().toString());
        log.info("============================================================");
        log.info("============================================================");
        log.info("/");
        log.info("/");

        CurrentEnvironmentManager.print();

        long finishTime = System.currentTimeMillis();
        log.info("启动成功，启动共耗时约：" + ((finishTime - startTime) / 1000) + " 秒 （" + (finishTime - startTime) + " 毫秒）");
        log.info("/");
        log.info("/");

        long webTs = TaobaoTime.getTimeStamp();
        if (webTs > 0) {
            long span = webTs - System.currentTimeMillis();
            String desc = "";
            if (span > 10 * 1000) desc = "（时间差较大（超过10秒），建议同步服务器系统时间）";
            log.info("时间检查：与网络时间差：" + span + " 毫秒" + desc);
        } else {
            log.info("时间检查：获取网络时间错误（值：" + webTs + "）");
        }
        log.info("/");
        log.info("/");
        log.info(HidocAppApplication.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        log.info(System.getProperty("java.class.path"));
        log.info("/");
        log.info("/");
    }
}
