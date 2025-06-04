package com.ligg.service.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Ligg
 * @Time 2025/6/4
 * 账单记录注解
 * 用于标记需要记录账单信息的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bill {

    /**
     * 操作类型描述
     */
    String operation() default "购买操作";

    /**
     * 是否记录详细信息
     */
    boolean detailed() default true;

    /**
     * 自定义业务标识
     */
    String businessType() default "PURCHASE";
}
