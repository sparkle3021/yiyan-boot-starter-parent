package com.yiyan.boot.cache.core.service;

/**
 * Redis 缓存服务接口
 *
 * @author MENGJIAO
 * @createDate 2023-09-28 下午 02:32
 */
public interface CacheService {
    /**
     * 从缓存中获取数据
     */
    Object getFromCache(String cacheName, Object cacheKey);

    /**
     * 缓存数据
     */
    boolean save(String[] cacheNames, Object cacheKey, Object cacheValue, long ttl);

    /**
     * 清除缓存
     */
    boolean invalidateCache(String[] cacheNames, Object cacheKey);

    /**
     * 清除缓存
     */
    boolean invalidateCache(String[] cacheNames);

    /**
     * 异步缓存数据
     */
    boolean saveInRedisAsync(String[] cacheNames, Object cacheKey, Object cacheValue, long ttl);

    /**
     * 异步清除缓存
     */
    boolean invalidateCacheInRedisAsync(String[] cacheNames, Object cacheKey);

    /**
     * 异步清除缓存
     */
    boolean invalidateCacheInRedisAsync(String[] cacheNames);
}
