package com.yiyan.boot.common.utils.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.yiyan.boot.common.enums.ErrorCodeEnum;
import com.yiyan.boot.common.exception.BizException;
import com.yiyan.boot.common.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
class CacheUtilTest {

    public static String TEST_CACHE_NAME = "TestCache";
    public static CaffeineCacheConfig caffeineCacheConfig = new CaffeineCacheConfig(TEST_CACHE_NAME, 100, 100, 2, TimeUnit.SECONDS);

    static {
        CacheUtil.createLocalCache(caffeineCacheConfig);
    }

    @Test
    void testCreateLocalCache() {
        // 预期：创建新的缓存，并可查询到缓存对象
        CacheUtil.createLocalCache(new CaffeineCacheConfig("cacheName-1"));
        CacheUtil.getLocalCacheNames().forEach(cacheName -> log.info("testCreateLocalCache: {}", cacheName));
    }

    @Test
    void testCreateLocalCacheList() {
        // 预期：创建新的缓存，并可查询到缓存对象
        List<CaffeineCacheConfig> list = new ArrayList<>();
        list.add(new CaffeineCacheConfig("cacheName-list-1"));
        list.add(new CaffeineCacheConfig("cacheName-list-2"));
        CacheUtil.createLocalCache(list);
        CacheUtil.getLocalCacheNames().forEach(cacheName -> log.info("testCreateLocalCacheList: {}", cacheName));

    }

    @Test
    void testRemoveLocalCache() {
        String cacheName = TEST_CACHE_NAME + System.currentTimeMillis();
        Cache<String, Object> result = CacheUtil.getLocalCache(cacheName, true);
        log.info("testRemoveLocalCache: 预期：创建新的缓存，result不为空 {}", ObjectUtils.isNotNull(result));

        CacheUtil.removeLocalCache(cacheName);
        try {
            CacheUtil.getLocalCache(cacheName);
        } catch (BizException e) {
            log.info("testRemoveLocalCache: 预期：缓存不存在，抛出异常 {}", Objects.equals(e.getErrorCode(), ErrorCodeEnum.LOCAL_CACHE_NOT_EXIST.getCode()));
        }
    }

    @Test
    void testGetLocalCache() {
        // 预期：创建新的缓存，result不为空
        Cache<String, Object> result = CacheUtil.getLocalCache(TEST_CACHE_NAME + System.currentTimeMillis(), true);
        log.info("testGetLocalCache: 预期：创建新的缓存，result不为空 {}", ObjectUtils.isNotNull(result));
    }

    @Test
    void testGetLocalCache2() {
        // 预期：缓存不存在，抛出异常
        try {
            CacheUtil.getLocalCache(TEST_CACHE_NAME + System.currentTimeMillis());
        } catch (BizException e) {
            log.info("testGetLocalCache2: {}", Objects.equals(e.getErrorCode(), ErrorCodeEnum.LOCAL_CACHE_NOT_EXIST.getCode()));
        }
    }

    @Test
    void testGetLocalCache3() {
        // 预期：缓存存在，result不为空
        Cache<String, Object> result = CacheUtil.getLocalCache(TEST_CACHE_NAME);
        log.info("testGetLocalCache3: 预期：缓存存在，result不为空 {}", ObjectUtils.isNotNull(result));
    }

    @Test
    void testCacheOptions() throws InterruptedException {
        // 插入缓存数据
        CacheUtil.put(TEST_CACHE_NAME, "key-1", "value-1");
        // 获取缓存数据，预期：value-1
        Object value = CacheUtil.get(TEST_CACHE_NAME, "key-1");
        log.info("testCacheOptions: 获取缓存数据，预期：value-1 : {}", value);
        // 更新缓存数据
        CacheUtil.put(TEST_CACHE_NAME, "key-1", "value-2");
        // 获取缓存数据，预期：value-2
        value = CacheUtil.get(TEST_CACHE_NAME, "key-1");
        log.info("testCacheOptions: 获取缓存数据，预期：value-2 : {}", value);
        // 删除缓存数据
        CacheUtil.remove(TEST_CACHE_NAME, "key-1");
        // 获取缓存数据，预期：null
        value = CacheUtil.get(TEST_CACHE_NAME, "key-1");
        log.info("testCacheOptions: 获取缓存数据，预期：null : {}", value);
        // 异步插入缓存数据
        CacheUtil.putAsync(TEST_CACHE_NAME, "key-async-1", "value-1");
        // 获取缓存数据，预期：value-1 | null
        value = CacheUtil.get(TEST_CACHE_NAME, "key-async-1");
        log.info("testCacheOptions: 获取缓存数据，预期：value-1 | null : {}", value);
        Thread.sleep(1000);
        // 获取缓存数据，预期：value-1
        value = CacheUtil.get(TEST_CACHE_NAME, "key-async-1");
        log.info("testCacheOptions: 获取缓存数据，预期：value-1 : {}", value);
        // 移除所有缓存数据
        CacheUtil.removeAll(TEST_CACHE_NAME);
        // 获取缓存数据，预期：null
        value = CacheUtil.get(TEST_CACHE_NAME, "key-async-1");
        log.info("testCacheOptions: 获取缓存数据，预期：null : {}", value);
    }

    @Test
    void testBatchPut() {
        // 批量插入缓存数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("key-1", "value-1");
        map.put("key-2", "value-2");
        CacheUtil.putAll(TEST_CACHE_NAME, map);

        // 获取缓存数据，预期：value-1
        Object value = CacheUtil.get(TEST_CACHE_NAME, "key-1");
        log.info("testBatchPut: 获取缓存数据，预期：value-1 : {}", value);
        // 获取缓存数据，预期：value-2
        value = CacheUtil.get(TEST_CACHE_NAME, "key-2");
        log.info("testBatchPut: 获取缓存数据，预期：value-2 : {}", value);
    }

    @Test
    void testAsyncBatchPut() throws InterruptedException {
        // 批量插入缓存数据
        HashMap<String, Object> map = new HashMap<>();
        map.put("key-async-1", "value-1");
        map.put("key-async-2", "value-2");
        CacheUtil.putAllAsync(TEST_CACHE_NAME, map);
        // 获取缓存数据，预期：value-1 | null
        Object value = CacheUtil.get(TEST_CACHE_NAME, "key-async-1");
        log.info("testAsyncBatchPut: 获取缓存数据，预期：value-1 | null : {}", value);
        // 获取缓存数据，预期：value-2 | null
        value = CacheUtil.get(TEST_CACHE_NAME, "key-async-2");
        log.info("testAsyncBatchPut: 获取缓存数据，预期：value-2 | null : {}", value);
        Thread.sleep(1000);
        // 获取缓存数据，预期：value-1
        value = CacheUtil.get(TEST_CACHE_NAME, "key-async-1");
        log.info("testAsyncBatchPut: 获取缓存数据，预期：value-1 : {}", value);
        // 获取缓存数据，预期：value-2
        value = CacheUtil.get(TEST_CACHE_NAME, "key-async-2");
        log.info("testAsyncBatchPut: 获取缓存数据，预期：value-2 : {}", value);
    }

    @Test
    void testCacheExpire() throws InterruptedException {
        // 插入缓存数据
        CacheUtil.put(TEST_CACHE_NAME, "key", "value");
        // 获取缓存数据，预期：value
        Object value = CacheUtil.get(TEST_CACHE_NAME, "key");
        log.info("testCacheExpire: 获取缓存数据，预期：value : {}", value);
        // 等待缓存过期
        log.info("等待缓存过期：{}秒", caffeineCacheConfig.getDuration());
        Thread.sleep(caffeineCacheConfig.getDuration() * 1000);
        // 获取缓存数据，预期：null
        value = CacheUtil.get(TEST_CACHE_NAME, "key");
        log.info("testCacheExpire: 获取缓存数据，预期：null : {}", value);
    }
}
