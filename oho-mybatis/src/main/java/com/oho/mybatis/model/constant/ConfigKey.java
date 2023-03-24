package com.oho.mybatis.model.constant;

import java.math.RoundingMode;

/**
 * @author Sparkler
 * @createDate 2022/11/13
 */
public class ConfigKey {
    public static final int RANDOM_INT_MIN = 1;
    public static final int RANDOM_INT_MAX = 10086;

    public static final long RANDOM_LONG_MIN = 1;
    public static final long RANDOM_LONG_MAX = 202211111024L;

    public static final int RANDOM_DATE_MIN = -9999;
    public static final int RANDOM_DATE_MAX = 9999;

    public static final int DEFAULT_DECIMAL_SCALE = 4;
    public static final RoundingMode DECIMAL_ROUNDING_MODE = RoundingMode.HALF_UP;
    /**
     * 单次插入数据量
     */
    public static final int ONCE_INSERT_RECORDS = 2000;

    /**
     * 批量插入线程数
     */
    public static final int MAX_BATCH_THREAD_SIZE = 20;
}
