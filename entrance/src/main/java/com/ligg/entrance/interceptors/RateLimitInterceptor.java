package com.ligg.entrance.interceptors;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;

/**
 * 请求拦截器
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimiter rateLimiter;

    public RateLimitInterceptor() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(100)// 每秒最多访问100次
                .limitRefreshPeriod(Duration.ofSeconds(2))// 限流时间间隔，单位为秒
                .timeoutDuration(Duration.ZERO)
                .build();
        this.rateLimiter = RateLimiterRegistry.of(config).rateLimiter("api");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (rateLimiter.acquirePermission()) {
            return true;
        }else {
            //使用枚举设置响应码
            response.setStatus(429);
            response.getWriter().write("请求频繁");
            return false;
        }
    }
}
