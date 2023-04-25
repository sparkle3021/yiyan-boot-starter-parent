package com.oho.common.utils.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final String DEFAULT_DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final String DEFAULT_DATE_FORMAT_PATTERN = "yyyy-MM-dd";

    /**
     * 将LocalDateTime对象转换为指定格式的字符串
     *
     * @param localDateTime LocalDateTime对象
     * @param pattern       格式化模式
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(formatter);
    }

    /**
     * 将指定格式的字符串解析为LocalDateTime对象
     *
     * @param dateTimeStr 待解析字符串
     * @param pattern     格式化模式
     * @return LocalDateTime对象
     */
    public static LocalDateTime parse(String dateTimeStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateTimeStr, formatter);
    }

    /**
     * 将LocalDateTime对象转换成默认的格式yyyy-MM-dd HH:mm:ss的字符串
     *
     * @param localDateTime LocalDateTime对象
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime localDateTime) {
        return format(localDateTime, DEFAULT_DATE_TIME_FORMAT_PATTERN);
    }

    /**
     * 将默认格式yyyy-MM-dd HH:mm:ss的字符串解析为LocalDateTime对象
     *
     * @param dateTimeStr 待解析字符串
     * @return LocalDateTime对象
     */
    public static LocalDateTime parse(String dateTimeStr) {
        return parse(dateTimeStr, DEFAULT_DATE_TIME_FORMAT_PATTERN);
    }

    /**
     * 将LocalDate对象转换为指定格式的字符串
     *
     * @param localDate LocalDate对象
     * @param pattern   格式化模式
     * @return 格式化后的字符串
     */
    public static String format(LocalDate localDate, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDate.format(formatter);
    }

    /**
     * 将指定格式的字符串解析为LocalDate对象
     *
     * @param dateStr 待解析字符串
     * @param pattern 格式化模式
     * @return LocalDate对象
     */
    public static LocalDate parseDate(String dateStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(dateStr, formatter);
    }

    /**
     * 将LocalDate对象转换成默认的格式yyyy-MM-dd的字符串
     *
     * @param localDate LocalDate对象
     * @return 格式化后的字符串
     */
    public static String formatDate(LocalDate localDate) {
        return format(localDate, DEFAULT_DATE_FORMAT_PATTERN);
    }

    /**
     * 将默认格式yyyy-MM-dd的字符串解析为LocalDate对象
     *
     * @param dateStr 待解析字符串
     * @return LocalDate对象
     */
    public static LocalDate parseDate(String dateStr) {
        return parseDate(dateStr, DEFAULT_DATE_FORMAT_PATTERN);
    }

    /**
     * 获取当前时间
     *
     * @return LocalDateTime对象
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * 获取当前日期
     *
     * @return LocalDate对象
     */
    public static LocalDate today() {
        return LocalDate.now();
    }

    /**
     * 获取当前时间戳
     *
     * @return 当前时间戳
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
