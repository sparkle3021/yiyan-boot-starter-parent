package com.yiyan.boot.common.utils.cache;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.yiyan.boot.common.utils.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * Caffeine 缓存工具类
 * </p>
 * 默认配置：最大缓存条数：2000，过期时间：7200秒
 * 使用时，可通过 {@link #instance(int, int, TimeUnit)} 方法初始化配置
 * </P>
 * initialCapacity=[integer]: 初始的缓存空间大小
 * maximumSize=[long]: 缓存的最大条数
 * maximumWeight=[long]: 缓存的最大权重
 * expireAfterAccess=[duration]: 最后一次写入或访问后经过固定时间过期
 * expireAfterWrite=[duration]: 最后一次写入后经过固定时间过期
 * refreshAfterWrite=[duration]: 创建缓存或者最近一次更新缓存后经过固定的时间间隔，刷新缓存
 * weakKeys: 打开key的弱引用
 * weakValues：打开value的弱引用
 * softValues：打开value的软引用
 * recordStats：开发统计功能
 * 注意：
 * expireAfterWrite和expireAfterAccess同事存在时，以expireAfterWrite为准。
 * maximumSize和maximumWeight不可以同时使用
 * weakValues和softValues不可以同时使用
 *
 * @author MENGJIAO
 * @createDate 2023-05-30 12:13
 */
@Slf4j
@Component
public class CacheUtil {

    /**
     * 默认配置
     */
    private static Cache<String, Object> cache = Caffeine.newBuilder()
            // 初始化缓存空间大小
            .initialCapacity(100)
            // 设置最大缓存条数
            .maximumSize(2000)
            // 设置写入后的默认过期时间
            .expireAfterWrite(7200, TimeUnit.SECONDS)
            .build();

    /**
     * 初始化缓存配置
     *
     * @param maxiMumSize 最大缓存条数
     * @param duration    过期时间
     * @param timeUnit    时间单位
     */
    public static void instance(int maxiMumSize, int duration, TimeUnit timeUnit) {
        cache = Caffeine.newBuilder()
                // 初始化缓存空间大小
                .initialCapacity(100)
                // 设置最大缓存条数
                .maximumSize(maxiMumSize)
                // 设置写入后的默认过期时间
                .expireAfterWrite(duration, timeUnit)
                // 设置缓存的移除通知
                .removalListener((key, value, cause) -> log.info("key: [{}], value: [{}], 删除原因: [{}]", key, value, cause))
                .build();
        log.info("Caffeine - 缓存初始化 - 最大缓存条数: [{}], 过期时间: [{}], 时间单位: [{}]", maxiMumSize, duration, timeUnit);
    }

    /**
     * 添加缓存
     *
     * @param key   缓存键
     * @param value 缓存值
     */
    public static void put(String key, Object value) {
        cache.put(key, value);
        log.info("Caffeine - 缓存插入 - key: [{}], value: [{}]", key, value);
    }

    /**
     * 获取缓存
     *
     * @param key 缓存键
     * @return 缓存值
     */
    public static Object get(String key) {
        return cache.getIfPresent(key);
    }

    /**
     * 移除缓存
     *
     * @param key 缓存键
     */
    public static void remove(String key) {
        cache.invalidate(key);
        log.info("Caffeine - 缓存移除 - key: [{}]", key);
    }

    /**
     * 移除所有缓存
     */
    public static void clearAll() {
        cache.invalidateAll();
    }

    /**
     * 批量添加缓存
     *
     * @param map 缓存键值对集合
     */
    public static void putAll(Map<String, Object> map) {
        cache.putAll(map);
        log.info("Caffeine - 缓存批量插入 - map: [{}]", JsonUtils.toJson(map));
    }

    /**
     * 异步批量添加缓存
     *
     * @param map 缓存键值对集合
     */
    public static void putAllAsync(Map<String, Object> map) {
        ConcurrentMap<String, Object> concurrentMap = cache.asMap();
        concurrentMap.putAll(map);
        log.info("Caffeine - 缓存异步批量插入 - map: [{}]", JsonUtils.toJson(map));
    }

    /**
     * 异步获取缓存
     *
     * @param key 缓存键
     * @return 缓存值
     */
    public static CompletableFuture<Object> getAsync(String key) {
        return CompletableFuture.supplyAsync(() -> cache.getIfPresent(key));
    }
}
