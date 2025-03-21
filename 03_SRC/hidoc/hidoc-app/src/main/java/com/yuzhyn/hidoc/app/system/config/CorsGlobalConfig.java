package com.yuzhyn.hidoc.app.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 在 Spring Boot 3.4 中
 * allowedOrigins 被弃用，推荐使用 allowedOriginPatterns 来配置允许的来源，这样可以使用更灵活的匹配模式。
 *
 * 虽然核心的跨域配置思路未变，但在 Spring Boot 3.4 中配置方式有所调整
 * 更推荐使用 CorsConfigurationSource Bean 来配置跨域。
 */
@Configuration
public class CorsGlobalConfig {

    @Bean
    public CorsFilter corsFilter() {
        // 创建 CORS 配置对象
        CorsConfiguration config = new CorsConfiguration();
        // 允许所有来源进行跨域调用，这里使用 addAllowedOriginPattern("*") 支持 Spring Boot 3.x 以上版本
        config.addAllowedOriginPattern("*");
        // 允许任何请求头
        config.addAllowedHeader("*");
        // 允许任何请求方法（如 GET、POST 等）
        config.addAllowedMethod("*");
        // 允许携带凭证（如 Cookie 等）
        config.setAllowCredentials(true);

        // 创建基于 URL 的 CORS 配置源
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 将 CORS 配置应用到所有接口路径
        source.registerCorsConfiguration("/**", config);

        // 创建并返回 CORS 过滤器
        return new CorsFilter(source);
    }
}