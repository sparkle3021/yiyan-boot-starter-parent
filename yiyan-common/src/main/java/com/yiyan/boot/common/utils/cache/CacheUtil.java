package com.yiyan.boot.common.utils.cache;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.yiyan.boot.common.enums.ErrorCodeEnum;
import com.yiyan.boot.common.exception.Asserts;
import com.yiyan.boot.common.utils.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Caffeine 缓存工具类
 * <p>
 * 适用于单机缓存，不适用于分布式缓存
 * 默认配置：初始化缓存大小为100，最大缓存条数：2000，过期时间：7200秒
 * </P>
 *
 * @author MENGJIAO
 * @createDate 2023-05-30 12:13
 */
@Slf4j
@Component
public class CacheUtil {

    /**
     * 本地缓存列表
     */
    private static Map<String, Cache<String, Object>> LOCAL_CACHE_MAP = new ConcurrentHashMap<>();

    /**
     * 创建本地缓存
     *
     * @param config 缓存配置
     */
    public static void createLocalCache(@Valid CaffeineCacheConfig config) {
        // 判断缓存是否已存在
        Cache<String, Object> localCache = LOCAL_CACHE_MAP.get(config.getCacheName());
        if (localCache != null) {
            Asserts.fail(ErrorCodeEnum.LOCAL_CACHE_EXIST);
        }
        Caffeine<Object, Object> builder = Caffeine.newBuilder();
        // 设置缓存初始大小，应该合理设置，后续扩容会影响性能
        builder.initialCapacity(config.getInitialCapacity());
        // 设置缓存最大条数，超过此数量时，会按照LRU最近最少使用算法来移除缓存项，-1不设置maximumSize
        if (config.getMaximumSize() > 0) {
            builder.maximumSize(config.getMaximumSize());
        }
        // 设置写入后的过期时间
        builder.expireAfterWrite(config.getDuration(), config.getTimeUnit());
        LOCAL_CACHE_MAP.put(config.getCacheName(), builder.build());
        log.info("Caffeine - 创建本地缓存 - cacheName: [{}], 最大缓存条数: [{}], 过期时间: [{}], 时间单位: [{}]", config.getCacheName(), config.getMaximumSize(), config.getDuration(), config.getTimeUnit());
    }

    /**
     * 创建本地缓存
     *
     * @param configs 缓存配置列表
     */
    public static void createLocalCache(@Valid List<CaffeineCacheConfig> configs) {
        configs.forEach(CacheUtil::createLocalCache);
    }

    /**
     * 获取本地缓存名称列表
     *
     * @return 缓存名称列表
     */
    public static List<String> getLocalCacheNames() {
        return new ArrayList<>(LOCAL_CACHE_MAP.keySet());
    }

    /**
     * 获取本地缓存
     *
     * @param cacheName 缓存名称
     * @param toCreate  不存在时是否创建
     * @return 缓存对象
     */
    public static Cache<String, Object> getLocalCache(String cacheName, boolean toCreate) {
        Cache<String, Object> localCache = LOCAL_CACHE_MAP.get(cacheName);
        if (toCreate) {
            createLocalCache(new CaffeineCacheConfig(cacheName));
            localCache = LOCAL_CACHE_MAP.get(cacheName);
        } else if (localCache == null) {
            Asserts.fail(ErrorCodeEnum.LOCAL_CACHE_NOT_EXIST);
        }
        return localCache;
    }

    /**
     * 获取本地缓存
     *
     * @param cacheName 缓存名称
     * @return 缓存对象
     */
    public static Cache<String, Object> getLocalCache(String cacheName) {
        return getLocalCache(cacheName, false);
    }

    /**
     * 移除本地缓存
     *
     * @param cacheName 缓存名称
     */
    public static void removeLocalCache(String cacheName) {
        Cache<String, Object> localCache = getLocalCache(cacheName);
        // 清除缓存
        localCache.invalidateAll();
        // 移除缓存
        LOCAL_CACHE_MAP.remove(cacheName);
        log.info("Caffeine - 移除本地缓存 - cacheName: [{}]", cacheName);
    }

    /**
     * 添加缓存
     *
     * @param cacheName 缓存名称
     * @param key       缓存键
     * @param value     缓存值
     */
    public static void put(String cacheName, String key, Object value) {
        getLocalCache(cacheName).put(key, value);
        log.info("Caffeine - 缓存插入 - CacheName [{}] , key: [{}], value: [{}]", cacheName, key, value);
    }

    /**
     * 异步添加缓存
     *
     * @param cacheName 缓存名称
     * @param key       缓存键
     * @param value     缓存值
     */
    public static void putAsync(String cacheName, String key, Object value) {
        CompletableFuture.runAsync(() -> put(cacheName, key, value));
    }

    /**
     * 获取缓存
     *
     * @param cacheName 缓存名称
     * @param key       缓存键
     * @return 缓存值
     */
    public static Object get(String cacheName, String key) {
        log.info("Caffeine - 获取缓存 - CacheName [{}] , key: [{}]", cacheName, key);
        return getLocalCache(cacheName).getIfPresent(key);
    }

    /**
     * 移除缓存
     *
     * @param cacheName 缓存名称
     * @param key       缓存键
     */
    public static void remove(String cacheName, String key) {
        getLocalCache(cacheName).invalidate(key);
        log.info("Caffeine - 缓存移除 - CacheName: [{}] , key: [{}]", cacheName, key);
    }

    /**
     * 移除所有缓存
     *
     * @param cacheName 缓存名称
     */
    public static void removeAll(String cacheName) {
        getLocalCache(cacheName).invalidateAll();
        log.info("Caffeine - 移除所有缓存 - CacheName: [{}]", cacheName);
    }

    /**
     * 批量添加缓存
     *
     * @param cacheName 缓存名称
     * @param map       缓存键值对集合
     */
    public static void putAll(String cacheName, Map<String, Object> map) {
        getLocalCache(cacheName).putAll(map);
        log.info("Caffeine - 缓存批量插入 - CacheName: [{}] , map: [{}]", cacheName, JsonUtils.toJson(map));
    }

    /**
     * 异步批量添加缓存
     *
     * @param cacheName 缓存名称
     * @param map       缓存键值对集合
     */
    public static void putAllAsync(String cacheName, Map<String, Object> map) {
        CompletableFuture.runAsync(() -> putAll(cacheName, map));
    }
}
