package com.ligg.common.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DatePathUtils {

    private static final DateTimeFormatter YEAR_FORMATTER = DateTimeFormatter.ofPattern("yyyy");
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MM");
    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("dd");

    /**
     * 获取年/月/日三级目录路径，格式：yyyy/MM/dd
     */
    public static String generateYearMonthDayPath() {
        return generateYearMonthDayPath(null);
    }

    /**
     * 按指定日期获取年/月/日三级目录路径，格式：yyyy/MM/dd
     *
     * @param date 指定日期，null 表示使用当前日期
     */
    public static String generateYearMonthDayPath(LocalDate date) {
        LocalDate targetDate = (date == null) ? LocalDate.now() : date;
        return String.join("/",
                YEAR_FORMATTER.format(targetDate),
                MONTH_FORMATTER.format(targetDate),
                DAY_FORMATTER.format(targetDate)
        );
    }
}
