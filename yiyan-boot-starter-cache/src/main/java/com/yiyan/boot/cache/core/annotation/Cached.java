package com.yiyan.boot.cache.core.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 新增缓存
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cached {

    /**
     * 缓存名称
     */
    String cacheName() default "";

    /**
     * 缓存KEY(唯一性）
     */
    String cacheKay() default "";

    /**
     * 缓存生命周期 (单位：秒）
     */
    long ttl() default 0;

    /**
     * 是否异步
     */
    boolean isAsync() default false;
}