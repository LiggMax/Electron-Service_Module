package com.ligg.common.constant;

/**
 * @Author Ligg
 * @Time 2025/6/16
 * <p>
 * 时间常量类
 **/
public final class TimeConstants {
    public static final long TEN = 10;

    public static final long TWENTY = 20;

    public static final long THIRTY = 30;

    public static final long FORTY = 40;

    public static final long FIFTY = 50;

    public static final long SIXTY = 60;


    /**
     * 毫秒单位转换（1秒 = 1000毫秒）
     */
    public static final long MILLISECONDS_PER_SECOND = 1000;

    /**
     * 获取指定秒数对应的毫秒数
     */
    public static long toMillis(long seconds) {
        return seconds * MILLISECONDS_PER_SECOND;
    }
}
