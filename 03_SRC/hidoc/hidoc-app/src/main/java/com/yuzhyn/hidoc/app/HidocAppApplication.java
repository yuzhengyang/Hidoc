package com.yuzhyn.hidoc.app;

import com.yuzhyn.azylee.core.systems.bases.SystemPropertyTool;
import com.yuzhyn.azylee.core.systems.bases.SystemStatusTool;
import com.yuzhyn.azylee.core.systems.bases.SystemTypeTool;
import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.file.File;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursor;
import com.yuzhyn.hidoc.app.application.model.sys.UserInfo;
import com.yuzhyn.hidoc.app.manager.CurrentEnvironmentManager;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@Slf4j
@EnableCaching
@SpringBootApplication
public class HidocAppApplication {

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        R.Caches.CacheManager = CacheManagerBuilder.newCacheManagerBuilder().build();
        R.Caches.CacheManager.init();
        R.Caches.UserInfo = R.Caches.CacheManager.createCache("UserInfo",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, UserInfo.class, ResourcePoolsBuilder.heap(1024)));
        R.Caches.SysFile = R.Caches.CacheManager.createCache("SysFile",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, File.class, ResourcePoolsBuilder.heap(1024)));
        R.Caches.SysFileCursor = R.Caches.CacheManager.createCache("SysFileCursor",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, FileCursor.class, ResourcePoolsBuilder.heap(1024)));

        System.out.println("~");
        SpringApplication app = new SpringApplication(HidocAppApplication.class);
        app.run(args);

        // 获取当前环境和配置信息
        CurrentEnvironmentManager.add("log.level", (log.isTraceEnabled() ? "trace," : "") +
                (log.isDebugEnabled() ? "debug," : "") + (log.isInfoEnabled() ? "info," : "") +
                (log.isWarnEnabled() ? "warn," : "") + (log.isErrorEnabled() ? "error" : ""));
        CurrentEnvironmentManager.add("system", SystemTypeTool.getOSname());
        CurrentEnvironmentManager.add("user.dir", SystemPropertyTool.userDir());

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

    }
}
