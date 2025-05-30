package com.ligg.common.aspect;

import com.ligg.common.annotation.RequireAuth;
import com.ligg.common.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * 权限验证切面
 * 用于拦截带有@RequireAuth注解的方法进行权限验证
 *
 * @author Ligg
 */
@Slf4j
@Aspect
@Component
public class AuthAspect {

    @Value("${minio.uploadKey}")
    private String uploadKey;

    /**
     * 拦截带有@RequireAuth注解的方法
     */
    @Around("@annotation(com.ligg.common.annotation.RequireAuth) || @within(com.ligg.common.annotation.RequireAuth)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 优先获取方法上的注解，如果没有则获取类上的注解
        RequireAuth requireAuth = method.getAnnotation(RequireAuth.class);
        if (requireAuth == null) {
            requireAuth = method.getDeclaringClass().getAnnotation(RequireAuth.class);
        }

        // 如果注解存在且需要验证权限
        if (requireAuth != null && requireAuth.required()) {
            // 进行权限验证
            checkAuth(requireAuth);
        }

        // 继续执行原方法
        return joinPoint.proceed();
    }

    /**
     * 权限验证逻辑
     */
    private void checkAuth(RequireAuth requireAuth) {
        // 获取当前请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.warn("无法获取当前请求上下文");
            throw new AuthException(requireAuth.errorCode(), requireAuth.errorMessage());
        }

        HttpServletRequest request = attributes.getRequest();

        // 获取Authorization头
        String auth = request.getHeader("Authorization");

        // 验证权限
        if (auth == null || !auth.equals(uploadKey)) {
            log.warn("权限验证失败: 请求URI={}, Authorization={}",
                    request.getRequestURI(), auth != null ? "***" : "null");
            throw new AuthException(requireAuth.errorCode(), requireAuth.errorMessage());
        }

        log.debug("权限验证成功: 请求URI={}", request.getRequestURI());
    }
} 