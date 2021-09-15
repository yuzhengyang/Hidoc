package com.yuzhyn.hidoc.app;

import com.yuzhyn.hidoc.app.aarg.R;
import com.yuzhyn.hidoc.app.application.internal.entity.SysFile;
import com.yuzhyn.hidoc.app.application.internal.entity.SysFileCursor;
import com.yuzhyn.hidoc.app.application.internal.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import pers.yuzhyn.azylee.core.systems.commons.SystemPropertyTool;
import pers.yuzhyn.azylee.core.systems.commons.SystemTypeTool;
import reactor.util.function.Tuple2;

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
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, SysFile.class, ResourcePoolsBuilder.heap(1024)));
        R.Cache.SysFileCursor = R.Cache.CacheManager.createCache("SysFileCursor",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, SysFileCursor.class, ResourcePoolsBuilder.heap(1024)));


        log.info("~");
        SpringApplication.run(HidocAppApplication.class, args);
        log.info("/");
        log.info("/");
        log.info("============================================================");
        log.info("============================================================");
        log.info("***HidocDeployer***::CODE::LaunchedSuccessfully--留念");
        log.info("hidoc 服务启动成功");
        log.info("");
        log.info("操作系统：" + SystemTypeTool.getOSname().toString());
        if (!SystemTypeTool.isLinux()) {
            log.info("环境异常：由于大部分功能仅支持Linux系统，Windows系统运行时部分功能将不可用");
        }
        log.info("运行路径：" + SystemPropertyTool.userDir());
        log.info("============================================================");
        log.info("============================================================");
        log.info("/");
        log.info("/");
    }
}
