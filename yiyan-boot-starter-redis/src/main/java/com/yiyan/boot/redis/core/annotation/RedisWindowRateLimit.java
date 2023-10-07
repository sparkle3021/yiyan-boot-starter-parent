package com.yiyan.boot.redis.core.annotation;

import com.yiyan.boot.redis.core.enums.LimitLevel;
import com.yiyan.boot.redis.core.enums.LimitType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Redis 滑动窗口限流注解
 *
 * @author MENGJIAO
 * @createDate 2023-10-07 上午 10:00
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisWindowRateLimit {

    /**
     * 限流类型，默认滑动窗口限流
     */
    LimitType type() default LimitType.SLIDING_WINDOW_RATE_LIMIT;

    /**
     * 限流级别，默认方法级限流
     * PS: 限流级别为IP时，KEY为IP地址
     */
    LimitLevel level() default LimitLevel.METHOD;

    /**
     * key的前缀,默认取方法全限定名
     *
     * @return key的前缀
     */
    String prefixKey() default "";

    /**
     * springEl 表达式
     * ps: #Parms,#ParmsArr[0],#ParmsObj.id
     *
     * @return 表达式
     */
    String key() default "";

    /**
     * 限流时间窗口
     *
     * @return 单位秒
     */
    int timeWindow();

    /**
     * 限流时间窗口内最大请求数
     */
    int maxPermits();
}
