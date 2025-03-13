package com.yuzhyn.hidoc.app.system.config;

import com.yuzhyn.hidoc.app.application.service.sys.SysUserLoginService;
import com.yuzhyn.hidoc.app.system.interceptor.Interceptor;
import com.yuzhyn.hidoc.app.system.interceptor.LogInterceptor;
import com.yuzhyn.hidoc.app.system.interceptor.SecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc增加拦截器配置
 */
@Configuration
public class AppWebMvcConfig implements WebMvcConfigurer {

    @Autowired
    SysUserLoginService sysUserLoginService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 实现跨域
        registry.addInterceptor(new Interceptor()).addPathPatterns("/**");
        registry.addInterceptor(new SecurityInterceptor(sysUserLoginService)).addPathPatterns("/**");
        // 日志记录
        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**");
    }
}