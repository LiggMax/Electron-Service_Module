package com.ligg.entrance.config;

import com.ligg.entrance.interceptors.LoginInterceptors;
import com.ligg.entrance.interceptors.RateLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${ignore.urls}")
    private String[] URLS;
    @Autowired
    private LoginInterceptors loginInterceptors;
    @Autowired
    private RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //登录拦截器
        registry.addInterceptor(loginInterceptors)
                .order(1)
                .excludePathPatterns("/api/admin/account/**")//放行路径
                .addPathPatterns("/api/admin/**");//拦截路径
        //请求拦截器
        registry.addInterceptor(rateLimitInterceptor)
                .order(2)
                .addPathPatterns("/**");
    }

    /**
     * 跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")// 允许跨域访问的路径
                .allowedOrigins(URLS) // 允许跨域访问的域名
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")// 允许跨域访问的方法
                .allowedHeaders("*")// 允许跨域访问的头
                .allowCredentials(true)// 是否允许发送cookie
                .maxAge(3600);// 预检间隔时间
    }
}
