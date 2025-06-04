package com.ligg.service.annotation;

import java.lang.annotation.*;

/**
 * 权限检查注解
 * 用于标记需要进行权限验证的方法
 *
 * @author Ligg
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireAuth {

    /**
     * 权限描述信息
     *
     * @return 权限描述
     */
    String value() default "需要授权访问";

    /**
     * 是否必须验证权限，默认为true
     *
     * @return 是否必须验证
     */
    boolean required() default true;

    /**
     * 权限验证失败时的错误码
     *
     * @return 错误码
     */
    int errorCode() default 401;

    /**
     * 权限验证失败时的错误信息
     *
     * @return 错误信息
     */
    String errorMessage() default "无权限访问";
} 