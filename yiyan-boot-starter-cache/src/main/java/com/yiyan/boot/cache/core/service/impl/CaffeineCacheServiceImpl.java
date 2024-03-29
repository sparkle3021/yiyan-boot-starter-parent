package com.yiyan.boot.cache.core.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.yiyan.boot.cache.autoconfigure.properties.MultiLayerCacheProperties;
import com.yiyan.boot.cache.core.service.CacheService;
import com.yiyan.boot.cache.core.service.RedisSendService;
import com.yiyan.boot.common.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * Caffeine缓存服务管理实现类
 *
 * @author MENGJIAO
 * @createDate 2023-09-28 下午 03:22
 */
@Slf4j
public class CaffeineCacheServiceImpl extends MultiCacheServiceImpl {

    private final MultiLayerCacheProperties multiLayerCacheProperties;
    private final RedisSendService redisSendService;
    /**
     * Caffeine内部缓存
     */
    private ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>();

    public CaffeineCacheServiceImpl(CacheService cacheService, MultiLayerCacheProperties multiLayerCacheProperties, RedisSendService redisSendService) {
        super(cacheService);
        this.multiLayerCacheProperties = multiLayerCacheProperties;
        this.redisSendService = redisSendService;
    }

    /**
     * 相当于在构建LoadingCache对象的时候 build()方法中指定过期之后的加载策略方法
     * 方法，refreshAfterWrite=60s属性才生效
     *
     * @return
     */
    public CacheLoader<Object, Object> cacheLoader() {
        CacheLoader<Object, Object> cacheLoader = new CacheLoader<Object, Object>() {
            @Override
            public Object load(Object key) throws Exception {
                return null;
            }

            // 重写这个方法将oldValue值返回回去，进而刷新缓存
            @Override
            public Object reload(Object key, Object oldValue) throws Exception {
                return oldValue;
            }
        };
        return cacheLoader;
    }

    /**
     * 初始化caffeine缓存对象
     *
     * @return
     */
    public Cache<Object, Object> caffeineCacheInit(long ttl) {
        MultiLayerCacheProperties.LocalCache cacheConfigProperties = multiLayerCacheProperties.getLocalCache();

        Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
        // Caffeine 缓存初始化参数配置
        // 设置初始缓存容量
        if (cacheConfigProperties.getInitialCapacity() > 0) {
            cacheBuilder.initialCapacity(cacheConfigProperties.getInitialCapacity());
        }
        // 设置缓存最大容量
        if (cacheConfigProperties.getMaximumSize() > 0) {
            cacheBuilder.maximumSize(cacheConfigProperties.getMaximumSize());
        }
        // 设置缓存最后一次写入后经过固定时间过期
        if (ttl > 0) {
            cacheBuilder.expireAfterWrite(ttl, TimeUnit.MILLISECONDS);
        }
        return cacheBuilder.build();
    }


    /**
     * 清理缓存（支持批量清理）
     *
     * @param cacheNames 缓存名称
     */
    private void clearAndSend(String[] cacheNames) {
        for (String cacheName : cacheNames) {
            clearAndSend(cacheName, null, false);
        }
        // 发送Redis缓存更新消息
        redisSendService.sendMessage(cacheNames, null);
    }

    /**
     * 清理缓存（支持批量清理）
     *
     * @param cacheNames 缓存名称
     * @param key        缓存key
     */
    private void clearAndSend(String[] cacheNames, Object key) {
        for (String cacheName : cacheNames) {
            clearAndSend(cacheName, key, false);
        }
        // 发送Redis缓存更新消息
        redisSendService.sendMessage(cacheNames, key);
    }


    /**
     * 保存并且发送缓存（支持批量清理）
     *
     * @param cacheNames 缓存名称
     * @param key        缓存key
     */
    private void saveAndSend(String[] cacheNames, Object key, long ttl, Object cacheValue) {
        for (String cacheName : cacheNames) {
            saveAndSend(cacheName, key, cacheValue, ttl, false);
        }
        // 发送Redis缓存更新消息, 所有cacheNames统一发送
        redisSendService.sendMessage(cacheNames, key);
    }


    /**
     * 清理缓存（支持批量清理）
     *
     * @param cacheNames 缓存名称
     * @param key        缓存key
     */
    public void clearNotSend(String[] cacheNames, Object key) {
        for (String cacheName : cacheNames) {
            clearAndSend(cacheName, key, false);
        }
    }


    /**
     * 保存本地缓存
     *
     * @param cacheName 缓存名称
     * @param key       缓存key
     */
    private void saveAndSend(String cacheName, Object key, Object value, long ttl, boolean isNeedSend) {
        // 获取缓存对象
        Cache caffeineCache = cacheMap.get(cacheName);
        if (caffeineCache == null) {
            caffeineCache = caffeineCacheInit(ttl);
            cacheMap.putIfAbsent(cacheName, caffeineCache);
        }
        caffeineCache.put(key, value);

        if (isNeedSend) {
            // 发送Redis缓存更新消息
            redisSendService.sendMessage(cacheName, key);
        }
    }


    /**
     * 保存更新Caffeine缓存
     *
     * @param cacheName     缓存名称
     * @param cacheKey      缓存key
     * @param result        缓存值
     * @param caffeineCache 缓存对象
     */
    private void saveCaffeineCache(String cacheName, Object cacheKey, Object result, long ttl, Cache caffeineCache) {
        if (null != result) {
            // 获取缓存对象
            if (caffeineCache == null) {
                caffeineCache = caffeineCacheInit(ttl);
                cacheMap.putIfAbsent(cacheName, caffeineCache);
            }
            caffeineCache.put(cacheKey, result);
        }
    }

    @Override
    public Object getFromCache(String cacheName, Object cacheKey, long... ttl) {
        Object cacheObj = null;
        Cache caffeineCache = cacheMap.get(cacheName);
        if (ObjectUtils.isNotNull(caffeineCache)) {
            // 从Caffeine缓存中获取
            cacheObj = caffeineCache.getIfPresent(cacheKey);
        }
        if (ObjectUtils.isNull(cacheObj)) {
            // 从Redis缓存中获取
            cacheObj = cacheService.getFromCache(cacheName, cacheKey);
            if (ObjectUtils.isNotNull(cacheObj)) {
                // 保存至Caffeine缓存
                saveCaffeineCache(cacheName, cacheKey, cacheObj, ttl[0], caffeineCache);
            }
        }
        return cacheObj;
    }

    @Override
    public boolean save(String[] cacheNames, Object cacheKey, Object cacheValue, long ttl) {
        boolean result = super.save(cacheNames, cacheKey, cacheValue, ttl);
        // 保存至Caffeine缓存,并发送Redis缓存更新消息
        saveAndSend(cacheNames[0], cacheKey, cacheValue, ttl, true);
        return result;
    }


    /**
     * 清除本地缓存
     *
     * @param cacheName 缓存名称
     * @param key       缓存key
     */
    private void clearAndSend(String cacheName, Object key, boolean isNeedSend) {
        // 获取缓存对象
        Cache caffeineCache = cacheMap.get(cacheName);
        if (caffeineCache == null) {
            return;
        }

        if (key == null) {
            // key键值为空， 则清空该缓存下面的所有条目
            caffeineCache.invalidateAll();
        } else {
            // 清除指定键值的缓存
            caffeineCache.invalidate(key);
        }

        if (isNeedSend) {
            // 发送Redis缓存更新消息
            redisSendService.sendMessage(cacheName, key);
        }
    }

    @Override
    public boolean invalidateCache(String[] cacheNames, Object cacheKey) {
        boolean result = super.invalidateCache(cacheNames, cacheKey);
        // 从Caffeine缓存中删除,并发送Redis缓存更新消息
        clearAndSend(cacheNames, cacheKey);
        return result;
    }

    @Override
    public boolean invalidateCache(String[] cacheNames) {
        boolean result = super.invalidateCache(cacheNames);
        // 从Caffeine缓存中删除,并发送Redis缓存更新消息
        clearAndSend(cacheNames);
        return result;
    }

    @Override
    public boolean saveInRedisAsync(String[] cacheNames, Object cacheKey, Object cacheValue, long ttl) {
        boolean result = super.saveInRedisAsync(cacheNames, cacheKey, cacheValue, ttl);
        // 保存至Caffeine缓存,并发送Redis缓存更新消息
        saveAndSend(cacheNames, cacheKey, ttl, cacheValue);
        return result;
    }

    @Override
    public boolean invalidateCacheInRedisAsync(String[] cacheNames, Object cacheKey) {
        boolean result = super.invalidateCacheInRedisAsync(cacheNames, cacheKey);
        // 从Caffeine缓存中删除,并发送Redis缓存更新消息
        clearAndSend(cacheNames, cacheKey);
        return result;
    }

    @Override
    public boolean invalidateCacheInRedisAsync(String[] cacheNames) {
        boolean result = super.invalidateCacheInRedisAsync(cacheNames);
        // 从Caffeine缓存中删除,并发送Redis缓存更新消息
        clearAndSend(cacheNames);
        return result;
    }
}
