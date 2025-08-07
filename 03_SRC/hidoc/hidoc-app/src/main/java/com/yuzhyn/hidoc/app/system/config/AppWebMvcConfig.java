package com.yuzhyn.hidoc.app.system.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.yuzhyn.hidoc.app.application.service.sys.SysUserLoginService;
import com.yuzhyn.hidoc.app.system.interceptor.LogInterceptor;
import com.yuzhyn.hidoc.app.system.interceptor.SecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * WebMvc增加拦截器配置
 */
@Configuration
public class AppWebMvcConfig implements WebMvcConfigurer {

    @Autowired
    SysUserLoginService sysUserLoginService;

//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        // 1. 先添加处理资源的转换器（优先级更高）
//        converters.add(new ResourceHttpMessageConverter());
//        // 2. 再添加FastJson转换器
//        converters.add(new FastJsonHttpMessageConverter());
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 实现跨域
//        registry.addInterceptor(new Interceptor()).addPathPatterns("/**");
        registry.addInterceptor(new SecurityInterceptor(sysUserLoginService)).addPathPatterns("/**");
        // 日志记录
        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**");
    }
}