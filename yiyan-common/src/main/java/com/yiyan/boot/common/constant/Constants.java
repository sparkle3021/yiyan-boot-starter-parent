package com.yiyan.boot.common.constant;

import java.util.concurrent.TimeUnit;

/**
 * 通用常量
 *
 * @author Sparkler
 * @createDate 2023/1/17
 */
public class Constants {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * www主域
     */
    public static final String WWW = "www.";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * Caffeine默认过期时间 2小时
     */
    public static final int CAFFEINE_DEFAULT_EXPIRE_TIME = 60 * 60 * 2;

    /**
     * Caffeine默认过期时间单位，秒
     */
    public static final TimeUnit CAFFEINE_DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

    /**
     * Caffeine默认初始缓存空间大小
     */
    public static final int CAFFEINE_DEFAULT_INITIAL_CAPACITY = 100;

    /**
     * Caffeine默认最大缓存数
     */
    public static final int CAFFEINE_DEFAULT_MAXIMUM_SIZE = 2000;


}
