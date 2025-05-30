package com.ligg.entrance.config;

import com.ligg.entrance.interceptors.LoginInterceptors;
import com.ligg.entrance.interceptors.RateLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
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
                //放行路径
                .excludePathPatterns("/api/admin/account/**",
                        "/api/user/account/**",
                        "/api/sms/**",
                        "/api/version/**",
                        "/api/adminWeb/account/**")
                .excludePathPatterns("/**/*.html", "/**/*.js", "/**/*.css", "/**/*.ico") // 放行静态资源
                .excludePathPatterns("/error") // 放行错误页面
                .excludePathPatterns("/ws/**", "/socket/**") // 放行WebSocket路径
                .addPathPatterns("/api/**");//拦截路径
        //请求拦截器
        registry.addInterceptor(rateLimitInterceptor)
                .order(2)
                .excludePathPatterns("/**/*.html", "/**/*.js", "/**/*.css", "/**/*.ico") // 放行静态资源
                .excludePathPatterns("/ws/**", "/socket/**") // 放行WebSocket路径
                .addPathPatterns("/**");
    }

    /**
     * 跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")// 允许跨域访问的路径
                .allowedOriginPatterns(URLS) // 使用通配符模式允许所有来源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")// 允许跨域访问的方法
                .allowedHeaders("*")// 允许跨域访问的头
                .exposedHeaders("*") // 允许客户端访问的响应头
                .allowCredentials(true)// 是否允许发送cookie
                .maxAge(3600);// 预检间隔时间
    }

    /**
     * 配置CORS过滤器，确保在拦截器之前处理跨域
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // 允许所有来源
        config.addAllowedHeader("*");
        config.addExposedHeader("*");
        config.addAllowedMethod("*");
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        source.registerCorsConfiguration("/ws/**", config); // 特别为WebSocket注册CORS配置

        return new CorsFilter(source);
    }
}
