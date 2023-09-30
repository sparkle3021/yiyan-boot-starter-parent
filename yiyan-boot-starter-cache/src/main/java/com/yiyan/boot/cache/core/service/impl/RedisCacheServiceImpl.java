package com.yiyan.boot.cache.core.service.impl;

import com.yiyan.boot.cache.core.service.CacheService;
import com.yiyan.boot.common.utils.ObjectUtils;
import com.yiyan.boot.common.utils.StringUtils;
import org.redisson.api.RBatch;
import org.redisson.api.RMapCache;
import org.redisson.api.RMapCacheAsync;
import org.redisson.api.RedissonClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存管理实现类
 *
 * @author MENGJIAO
 * @createDate 2023-09-28 下午 02:39
 */
public class RedisCacheServiceImpl implements CacheService {

    private final RedissonClient redissonClient;

    private final ExecutorService asyncCachePool;

    public RedisCacheServiceImpl(RedissonClient redissonClient, ExecutorService asyncCachePool) {
        this.redissonClient = redissonClient;
        this.asyncCachePool = asyncCachePool;
    }

    @Override
    public Object getFromCache(String cacheName, Object cacheKey) {
        if (StringUtils.isBlank(cacheName) || cacheKey == null) {
            throw new IllegalArgumentException("Cache name or cache key can not be null!");
        }
        return redissonClient.getMapCache(cacheName).get(cacheKey);
    }

    @Override
    public boolean save(String[] cacheNames, Object cacheKey, Object cacheValue, long ttl) {
        // 校验参数
        if (ObjectUtils.isEmpty(cacheKey) || 0 == cacheNames.length) {
            throw new IllegalArgumentException("Cache key or cache value can not be null!");
        }

        for (String cacheName : cacheNames) {
            RMapCache mapCache = redissonClient.getMapCache(cacheName);
            boolean isExist = mapCache.isExists();
            if (isExist) {
                // 已经缓存，直接更新
                mapCache.put(cacheKey, cacheValue, ttl, TimeUnit.SECONDS);
            } else {
                // 第一次缓存，设定超时时间
                RBatch batch = redissonClient.createBatch();
                RMapCacheAsync mapCacheAsync = batch.getMapCache(cacheName);
                mapCacheAsync.putAsync(cacheKey, cacheValue, ttl, TimeUnit.SECONDS);
                mapCacheAsync.expireAsync(ttl, TimeUnit.SECONDS);
                batch.execute();
            }
            redissonClient.getMapCache(cacheName).put(cacheKey, cacheValue, ttl, TimeUnit.SECONDS);
        }
        return true;
    }

    @Override
    public boolean invalidateCache(String[] cacheNames, Object cacheKey) {
        if (ObjectUtils.isEmpty(cacheKey)) {
            throw new IllegalArgumentException("Cache key can not be null!");
        }
        for (String cacheName : cacheNames) {
            if (StringUtils.isBlank(cacheName)) {
                continue;
            }
            if (null == redissonClient.getMapCache(cacheName)) {
                continue;
            }
            redissonClient.getMapCache(cacheName).remove(cacheKey);
        }
        return true;
    }

    @Override
    public boolean invalidateCache(String[] cacheNames) {
        for (String cacheName : cacheNames) {
            redissonClient.getMapCache(cacheName).delete();
        }
        return true;
    }

    @Override
    public boolean saveInRedisAsync(String[] cacheNames, Object cacheKey, Object cacheValue, long ttl) {
        asyncCachePool.execute(() -> save(cacheNames, cacheKey, cacheValue, ttl));
        return true;
    }

    @Override
    public boolean invalidateCacheInRedisAsync(String[] cacheNames, Object cacheKey) {
        asyncCachePool.execute(() -> invalidateCache(cacheNames, cacheKey));
        return true;
    }

    @Override
    public boolean invalidateCacheInRedisAsync(String[] cacheNames) {
        asyncCachePool.execute(() -> invalidateCache(cacheNames));
        return true;
    }
}
