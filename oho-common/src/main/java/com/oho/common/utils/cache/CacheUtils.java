package com.oho.common.utils.cache;

import cn.hutool.cache.CacheUtil;
import com.oho.common.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * 缓存工具类
 *
 * @author Sparkler
 * @createDate 2023/2/14-12:17
 */
@EnableCaching
@Slf4j
@Configuration
@Component
public class CacheUtils extends CacheUtil {

    /**
     * 获取Cache对象
     *
     * @param cacheName 缓存名称
     * @return 缓存Cache对象
     */
    private static Cache getCache(String cacheName) {
        CacheManager cacheManager = SpringContextUtils.getBean(CacheManager.class);
        return Objects.requireNonNull(cacheManager.getCache(cacheName), "缓存不存在：" + cacheName);
    }

    /**
     * 从缓存中获取数据
     *
     * @param cacheName 缓存名称
     * @param key       key
     * @param clazz     值类型
     * @param <T>       值类型
     * @return 缓存数据
     */
    public static <T> T get(String cacheName, Object key, Class<T> clazz) {
        Cache cache = getCache(cacheName);
        Cache.ValueWrapper valueWrapper = cache.get(key);
        if (valueWrapper == null) {
            return null;
        }
        return clazz.cast(valueWrapper.get());
    }

    /**
     * 从缓存中获取数据，并在缓存为空时回调方法获取数据
     *
     * @param cacheName 缓存名称
     * @param key       key
     * @param clazz     值类型
     * @param callable  回调方法
     * @param <T>       值类型
     * @return 缓存数据
     */
    public static <T> T get(String cacheName, Object key, Class<T> clazz, Callable<T> callable) {
        T result = get(cacheName, key, clazz);
        if (result != null) {
            return result;
        }
        try {
            result = callable.call();
        } catch (Exception e) {
            log.error("缓存不存在，回调方法执行失败", e);
            return null;
        }
        put(cacheName, key, result);
        return result;
    }

    /**
     * 将数据存入缓存中
     *
     * @param cacheName 缓存名称
     * @param key       key
     * @param value     数据值
     */
    public static void put(String cacheName, Object key, Object value) {
        Cache cache = getCache(cacheName);
        cache.put(key, value);
    }

    /**
     * 从缓存中移除数据
     *
     * @param cacheName 缓存名称
     * @param key       key
     */
    public static void remove(String cacheName, Object key) {
        Cache cache = getCache(cacheName);
        cache.evict(key);
    }

    /**
     * 清空缓存中的所有数据
     *
     * @param cacheName 缓存名称
     */
    public static void clear(String cacheName) {
        Cache cache = getCache(cacheName);
        cache.clear();
    }

    /**
     * 创建EhcacheManager Bean
     *
     * @return EhcacheManagerFactoryBean
     */
    @Bean
    public EhCacheManagerFactoryBean ehcacheCacheManager() {
        EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
        factoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        factoryBean.setShared(true);
        return factoryBean;
    }

    /**
     * 创建EhCacheCacheManager Bean
     *
     * @return EhCacheCacheManager
     */
    @Bean
    public CacheManager cacheManager() {
        EhCacheCacheManager cacheManager = new EhCacheCacheManager();
        cacheManager.setCacheManager(ehcacheCacheManager().getObject());
        return cacheManager;
    }
}
