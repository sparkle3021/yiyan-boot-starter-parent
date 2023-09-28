package com.yiyan.boot.common.constant;

/**
 * @author MengJiao
 * @createDate 2023/2/13-9:41
 */
public class NumberConstant {
    /**
     * 0
     */
    public static final int ZERO = 0;

    /**
     * 1
     */
    public static final int ONE = 1;

    /**
     * 半小时，单位秒。1800s
     */
    public static final int HALF_HOUR = 60 * 30;

    /**
     * 一小时，单位秒。3600s
     */
    public static final int ONE_HOUR = HALF_HOUR * 2;

    /**
     * 两小时，单位秒。7200s
     */
    public static final int TWO_HOURS = ONE_HOUR * 2;

    /**
     * 十二小时，单位秒。43200s
     */
    public static final int TWELVE_HOURS = ONE_HOUR * 12;

    /**
     * 一天，单位秒。86400s
     */
    public static final int ONE_DAY = ONE_HOUR * 24;

    /**
     * 一周，单位秒。604800s
     */
    public static final int ONE_WEEK = ONE_DAY * 7;

    /**
     * 一个月（30天），单位秒。2592000s
     */
    public static final int ONE_MONTH = ONE_DAY * 30;
}
