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
     *
     * @param cacheName 缓存名称
     * @param cacheKey  缓存KEY
     * @return 缓存数据
     */
    Object getFromCache(String cacheName, Object cacheKey);

    /**
     * 缓存数据
     *
     * @param cacheNames 缓存名称
     * @param cacheKey   缓存KEY
     * @param cacheValue 缓存数据
     * @param ttl        缓存时间
     * @return 是否缓存成功
     */
    boolean save(String[] cacheNames, Object cacheKey, Object cacheValue, long ttl);

    /**
     * 清除缓存
     *
     * @param cacheNames 缓存名称
     * @param cacheKey   缓存KEY
     * @return 是否清除成功
     */
    boolean invalidateCache(String[] cacheNames, Object cacheKey);

    /**
     * 清除缓存
     *
     * @param cacheNames 缓存名称
     * @return 是否清除成功
     */
    boolean invalidateCache(String[] cacheNames);

    /**
     * 异步缓存数据
     *
     * @param cacheNames 缓存名称
     * @param cacheKey   缓存KEY
     * @param cacheValue 缓存数据
     * @param ttl        缓存时间
     * @return 是否缓存成功
     */
    boolean saveInRedisAsync(String[] cacheNames, Object cacheKey, Object cacheValue, long ttl);

    /**
     * 异步清除缓存
     *
     * @param cacheNames 缓存名称
     * @param cacheKey   缓存KEY
     * @return 是否清除成功
     */
    boolean invalidateCacheInRedisAsync(String[] cacheNames, Object cacheKey);

    /**
     * 异步清除缓存
     *
     * @param cacheNames 缓存名称
     * @return 是否清除成功
     */
    boolean invalidateCacheInRedisAsync(String[] cacheNames);
}
