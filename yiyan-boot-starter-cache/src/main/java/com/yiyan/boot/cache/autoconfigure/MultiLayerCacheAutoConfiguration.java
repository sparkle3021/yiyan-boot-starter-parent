package com.yiyan.boot.cache.autoconfigure;

import com.yiyan.boot.cache.autoconfigure.properties.MultiLayerCacheProperties;
import com.yiyan.boot.cache.core.aspect.CacheManagerAspect;
import com.yiyan.boot.cache.core.config.NamedThreadFactory;
import com.yiyan.boot.cache.core.listener.CacheMessageListener;
import com.yiyan.boot.cache.core.model.CacheMessage;
import com.yiyan.boot.cache.core.service.CacheService;
import com.yiyan.boot.cache.core.service.RedisSendService;
import com.yiyan.boot.cache.core.service.impl.CaffeineCacheServiceImpl;
import com.yiyan.boot.cache.core.service.impl.RedisCacheServiceImpl;
import com.yiyan.boot.cache.core.service.impl.RedisSendServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 多级缓存自动配置
 *
 * @author MENGJIAO
 * @createDate 2023-09-28 下午 01:50
 */
@Slf4j
@ConditionalOnClass(Redisson.class)
@Configuration
@EnableConfigurationProperties(MultiLayerCacheProperties.class)
@ConditionalOnProperty(prefix = "multi.cache", name = "enable", havingValue = "true")
public class MultiLayerCacheAutoConfiguration {

    /**
     * 异步缓存线程池配置
     *
     * @param cacheConfigProperties 缓存配置属性
     * @return 异步缓存线程池
     */
    @Bean(name = "cacheAsyncExecutor")
    @ConditionalOnMissingBean
    public ExecutorService cacheAsyncExecutor(MultiLayerCacheProperties cacheConfigProperties) {
        log.info("[缓存] - [初始化异步缓存线程池]");
        log.info("[缓存] - [缓存异步线程池配置]：{}", cacheConfigProperties.getAsyncExecutor());
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
                cacheConfigProperties.getAsyncExecutor().getCorePoolSize(),
                cacheConfigProperties.getAsyncExecutor().getMaxPoolSize(),
                cacheConfigProperties.getAsyncExecutor().getKeepAliveSeconds(),
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(cacheConfigProperties.getAsyncExecutor().getQueueCapacity()),
                new NamedThreadFactory(cacheConfigProperties.getAsyncExecutor().getThreadNamePrefix()));
        poolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        log.info("[缓存] - [缓存异步线程池初始化成功]");
        return poolExecutor;
    }

    /**
     * 初始化Redis通讯服务
     *
     * @param redissonClient        redisson客户端
     * @param cacheConfigProperties 缓存配置属性
     * @return Redis通讯服务
     */
    @Bean
    public RedisSendService redisSendService(RedissonClient redissonClient, MultiLayerCacheProperties cacheConfigProperties) {
        log.info("[缓存] - [初始化Redis缓存更新消息发布服务]");
        log.info("[缓存] - [Redis 缓存更新消息发布TOPIC]：{}", cacheConfigProperties.getRemoteCache().getTopicName());
        RedisSendServiceImpl redisSendService = new RedisSendServiceImpl(redissonClient, cacheConfigProperties.getRemoteCache().getTopicName());
        log.info("[缓存] - [Redis 缓存更新消息发布服务初始化成功]");
        return redisSendService;
    }

    /**
     * 初始化缓存服务
     *
     * @param redissonClient        redisson客户端
     * @param redisSendService      redis消息发送服务
     * @param cacheAsyncExecutor    缓存异步线程池
     * @param cacheConfigProperties 缓存配置属性
     * @return 缓存服务
     */
    @Bean(name = "multiCacheService")
    @ConditionalOnBean(RedisSendService.class)
    public CacheService multiCacheService(RedissonClient redissonClient,
                                          RedisSendService redisSendService,
                                          ExecutorService cacheAsyncExecutor,
                                          MultiLayerCacheProperties cacheConfigProperties) {
        log.info("[缓存] - [初始化多级缓存服务]");
        log.info("[缓存] - 二级缓存开启状态：{}", cacheConfigProperties.isSecondCacheEnable());
        CacheService cacheService = null;
        // 判断是否开启二级缓存
        RedisCacheServiceImpl redisCacheService = new RedisCacheServiceImpl(redissonClient, cacheAsyncExecutor, cacheConfigProperties.getRemoteCache().getCachePrefixKey());
        if (cacheConfigProperties.isSecondCacheEnable()) {
            cacheService = new CaffeineCacheServiceImpl(redisCacheService, cacheConfigProperties, redisSendService);
        } else {
            cacheService = redisCacheService;
        }
        log.info("[缓存] - [多级缓存服务初始化成功]");
        return cacheService;
    }


    /**
     * 初始化Redis缓存消息订阅监听
     *
     * @param redissonClient        redisson客户端
     * @param caffeineCacheService  本地缓存服务
     * @param cacheConfigProperties 缓存配置属性
     * @return Redis缓存消息订阅监听
     */
    @Bean
    @ConditionalOnBean(CacheService.class)
    public RTopic subscribe(RedissonClient redissonClient, CacheService caffeineCacheService, MultiLayerCacheProperties cacheConfigProperties) {
        log.info("[缓存] - [初始化Redis缓存更新消息订阅监听]");
        log.info("[缓存] - [Redis 缓存更新消息订阅TOPIC]：{}", cacheConfigProperties.getRemoteCache().getTopicName());
        RTopic rTopic = redissonClient.getTopic(cacheConfigProperties.getRemoteCache().getTopicName());
        CacheMessageListener messageListener = new CacheMessageListener((CaffeineCacheServiceImpl) caffeineCacheService);
        rTopic.addListener(CacheMessage.class, messageListener);
        log.info("[缓存] - [Redis 缓存更新消息订阅监听初始化成功]");
        return rTopic;
    }

    @Bean
    @ConditionalOnBean(CacheService.class)
    public CacheManagerAspect cacheManagerAspect(
            CacheService cacheService,
            MultiLayerCacheProperties cacheConfigProperties) {
        return new CacheManagerAspect(cacheService, cacheConfigProperties);
    }
}
