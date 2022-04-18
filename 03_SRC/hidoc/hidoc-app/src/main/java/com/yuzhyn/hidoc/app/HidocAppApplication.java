package com.yuzhyn.hidoc.app;

import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.entity.file.File;
import com.yuzhyn.hidoc.app.application.entity.file.FileCursor;
import com.yuzhyn.hidoc.app.application.model.sys.UserInfo;
import com.yuzhyn.hidoc.app.manager.CurrentEnvironmentManager;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.embedded.TomcatWebServerFactoryCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import com.yuzhyn.azylee.core.systems.commons.SystemPropertyTool;
import com.yuzhyn.azylee.core.systems.commons.SystemTypeTool;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;

@Slf4j
@EnableCaching
@SpringBootApplication
public class HidocAppApplication {

    public static void main(String[] args) throws Exception {
        R.Cache.CacheManager = CacheManagerBuilder.newCacheManagerBuilder().build();
        R.Cache.CacheManager.init();
        R.Cache.UserInfo = R.Cache.CacheManager.createCache("UserInfo",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, UserInfo.class, ResourcePoolsBuilder.heap(1024)));
        R.Cache.SysFile = R.Cache.CacheManager.createCache("SysFile",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, File.class, ResourcePoolsBuilder.heap(1024)));
        R.Cache.SysFileCursor = R.Cache.CacheManager.createCache("SysFileCursor",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, FileCursor.class, ResourcePoolsBuilder.heap(1024)));

        System.out.println("~");
        SpringApplication app = new SpringApplication(HidocAppApplication.class);
        app.run(args);

        // 获取当前环境和配置信息
        CurrentEnvironmentManager.add("log.level", (log.isTraceEnabled() ? "trace," : "") +
                (log.isDebugEnabled() ? "debug," : "") + (log.isInfoEnabled() ? "info," : "") +
                (log.isWarnEnabled() ? "warn," : "") + (log.isErrorEnabled() ? "error" : ""));
        CurrentEnvironmentManager.add("system", SystemTypeTool.getOSname());
        CurrentEnvironmentManager.add("user.dir",SystemPropertyTool.userDir());

        log.info("/");
        log.info("============================================================");
        log.info("============================================================");
        log.info("");
        log.info("***HidocDeployer***::CODE::LaunchedSuccessfully");
        log.info("hidoc 服务启动成功");
        log.info("");
        log.info("============================================================");
        log.info("============================================================");
        log.info("/");
        log.info("/");

        CurrentEnvironmentManager.print();
    }
}
