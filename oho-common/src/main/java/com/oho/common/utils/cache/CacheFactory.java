package com.oho.common.utils.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 本地缓存工厂
 *
 * @author Sparkler
 */
@Slf4j
public class CacheFactory {

    /**
     * 获取实例
     */
    public static final CacheFactory INSTANCE = new CacheFactory();

    private CacheFactory() {
    }

    /**
     * 构建缓存
     *
     * @param expiration the expiration
     * @return cache cache
     */
    public Cache<String, Object> getCache(Long expiration) {
        return CacheBuilder.newBuilder()
                .concurrencyLevel(8)
                .expireAfterWrite(expiration, TimeUnit.SECONDS)
                .initialCapacity(20)
                .maximumSize(1000L)
                .recordStats()
                .removalListener(notification -> log.info("{}被移除了,原因:{}", notification.getKey(), notification.getCause()))
                .build();
    }
}
