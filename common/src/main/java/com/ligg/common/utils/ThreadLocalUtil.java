package com.ligg.common.utils;

@SuppressWarnings("all")
public class ThreadLocalUtil {
    private static final ThreadLocal THREAD_LOCAL = new ThreadLocal();

    // 获取数据
    public static  <T> T get(){
        return (T) THREAD_LOCAL.get();
    }

    // 设置数据
    public static void set(Object value){
        THREAD_LOCAL.set(value);
    }

    // 移除数据
    public static void remove(){
        THREAD_LOCAL.remove();
    }
}
