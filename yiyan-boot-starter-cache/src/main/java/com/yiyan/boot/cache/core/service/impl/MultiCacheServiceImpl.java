package com.yiyan.boot.cache.core.service.impl;

import com.yiyan.boot.cache.core.service.CacheService;

/**
 * 多级缓存服务管理抽象
 *
 * @author MENGJIAO
 * @createDate 2023-09-28 下午 03:30
 */
public abstract class MultiCacheServiceImpl implements CacheService {
    protected CacheService cacheService;

    public MultiCacheServiceImpl(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public Object getFromCache(String cacheName, Object cacheKey) {
        return cacheService.getFromCache(cacheName, cacheKey);
    }

    @Override
    public boolean save(String[] cacheNames, Object cacheKey, Object cacheValue, long ttl) {
        return cacheService.save(cacheNames, cacheKey, cacheValue, ttl);
    }

    @Override
    public boolean invalidateCache(String[] cacheNames, Object cacheKey) {
        return cacheService.invalidateCache(cacheNames, cacheKey);
    }

    @Override
    public boolean invalidateCache(String[] cacheNames) {
        return cacheService.invalidateCache(cacheNames);
    }

    @Override
    public boolean saveInRedisAsync(String[] cacheNames, Object cacheKey, Object cacheValue, long ttl) {
        return cacheService.saveInRedisAsync(cacheNames, cacheKey, cacheValue, ttl);
    }

    @Override
    public boolean invalidateCacheInRedisAsync(String[] cacheNames, Object cacheKey) {
        return cacheService.invalidateCacheInRedisAsync(cacheNames, cacheKey);
    }

    @Override
    public boolean invalidateCacheInRedisAsync(String[] cacheNames) {
        return cacheService.invalidateCacheInRedisAsync(cacheNames);
    }
}
