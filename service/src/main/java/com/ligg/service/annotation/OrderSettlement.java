package com.ligg.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Ligg
 * @Time 2025/1/15
 * 订单结算记录注解
 * 用于标记需要记录订单结算信息的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderSettlement {

    /**
     * 操作类型描述
     */
    String operation() default "订单结算操作";

    /**
     * 是否启用日志记录
     */
    boolean enableLog() default true;
} 