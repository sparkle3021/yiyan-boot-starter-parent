package com.yiyan.boot.cache.core.annotation;


import java.lang.annotation.*;


/**
 * 缓存删除
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface CacheDelete {

    /**
     * 缓存名称
     */
    String[] cacheNames() default {};

    /**
     * 缓存key（唯一性）
     */
    String cacheKey() default "";

    /**
     * 是否全部移除
     */
    boolean removeAll() default false;

    /**
     * 是否异步
     */
    boolean isAsync() default false;
}
