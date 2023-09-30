package com.yiyan.boot.cache.core.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 缓存更新
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CachePut {

    /**
     * 缓存名称
     */
    String[] cacheNames() default {};

    /**
     * 缓存key（唯一性）
     */
    String cacheKey();

    /**
     * 缓存生命周期(单位：秒）
     */
    long ttl() default 0;

    /**
     * 是否异步
     */
    boolean isAsync() default false;

}
