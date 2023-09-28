package com.yiyan.boot.cache.autoconfigure;

import com.yiyan.boot.cache.autoconfigure.properties.MultiLayerCacheProperties;
import com.yiyan.boot.cache.core.config.NamedThreadFactory;
import com.yiyan.boot.cache.core.listener.CacheMessageListener;
import com.yiyan.boot.cache.core.model.CacheData;
import com.yiyan.boot.cache.core.service.CacheService;
import com.yiyan.boot.cache.core.service.RedisSendService;
import com.yiyan.boot.cache.core.service.impl.CaffeineCacheServiceImpl;
import com.yiyan.boot.cache.core.service.impl.RedisCacheServiceImpl;
import com.yiyan.boot.cache.core.service.impl.RedisSendServiceImpl;
import com.yiyan.boot.common.utils.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.LZ4Codec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
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
@Configuration
@EnableConfigurationProperties(MultiLayerCacheProperties.class)
@ConditionalOnProperty(prefix = "multi.cache", name = "enable", havingValue = "true")
public class MultiLayerCacheAutoConfiguration {

    /**
     * 远程缓存配置 Redisson 客户端
     */
    @Bean
    @ConditionalOnMissingClass("org.redisson.api.RedissonClient")
    public RedissonClient redissonClient(MultiLayerCacheProperties cacheConfigProperties) {
        MultiLayerCacheProperties.RemoteCache remoteCache = cacheConfigProperties.getRemoteCache();
        Config config = new Config();
        RedissonClient redisson = null;
        if (null != remoteCache.getHost()) {
            // 单机连接方式
            SingleServerConfig serverConfig = config.useSingleServer().setAddress("redis://" + remoteCache.getHost() + ":" + remoteCache.getPort());
            serverConfig.setDatabase(remoteCache.getDatabase());
            serverConfig.setPassword(remoteCache.getPassword());
            serverConfig.setConnectionMinimumIdleSize(remoteCache.getMinIdle());
            serverConfig.setConnectionPoolSize(remoteCache.getMaxIdle());
            serverConfig.setConnectTimeout(remoteCache.getTimeout());
            serverConfig.setTimeout(remoteCache.getTimeout());
            redisson = Redisson.create(config);
        } else {
            if (null == remoteCache.getNodes()) {
                throw new RuntimeException("You need to config the clusterNodes property!");
            }
            // 集群连接方式
            ClusterServersConfig serversConfig = config.useClusterServers();
            serversConfig.setConnectTimeout(remoteCache.getTimeout());
            serversConfig.setTimeout(remoteCache.getTimeout());
            serversConfig.setMasterConnectionPoolSize(remoteCache.getPoolMaxSize());
            serversConfig.setSlaveConnectionPoolSize(remoteCache.getPoolMaxSize());
            serversConfig.setMasterConnectionMinimumIdleSize(remoteCache.getMinIdleSize());
            serversConfig.setSlaveConnectionMinimumIdleSize(remoteCache.getMinIdleSize());

            Arrays.stream(remoteCache.getNodes().split(",")).forEach(host -> serversConfig.addNodeAddress("redis://" + host.trim()));
            serversConfig.setPassword(remoteCache.getPassword());
            redisson = Redisson.create(config);
        }

        if (remoteCache.isUseCompression()) {
            // 开启压缩, 采用LZ4压缩
            redisson.getConfig().setCodec(new LZ4Codec());
        } else {
            redisson.getConfig().setCodec(new JsonJacksonCodec(JsonUtils.objectMapper));
        }
        return redisson;
    }

    @Bean
    @ConditionalOnBean(RedissonClient.class)
    public RedissonClient redissonClient(RedissonClient redissonClient, MultiLayerCacheProperties cacheConfigProperties) {
        log.info("redisson codec 配置");
        MultiLayerCacheProperties.RemoteCache remoteCache = cacheConfigProperties.getRemoteCache();
        if (remoteCache.isUseCompression()) {
            // 开启压缩, 采用LZ4压缩
            redissonClient.getConfig().setCodec(new LZ4Codec());
        } else {
            redissonClient.getConfig().setCodec(new JsonJacksonCodec(JsonUtils.objectMapper));
        }
        log.info("redisson codec 配置完成 ： {}", redissonClient);
        return redissonClient;
    }

    /**
     * 异步缓存线程池配置
     */
    @Bean
    @ConditionalOnMissingBean(name = {"redisExecutor"})
    public ExecutorService redisExecutor(@Qualifier("cacheConfigProperties") MultiLayerCacheProperties cacheConfigProperties) {
        log.info("初始化异步缓存线程池");
        log.info("异步缓存线程池配置：{}", cacheConfigProperties.getAsyncExecutor());
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
                cacheConfigProperties.getAsyncExecutor().getCorePoolSize(),
                cacheConfigProperties.getAsyncExecutor().getMaxPoolSize(),
                cacheConfigProperties.getAsyncExecutor().getKeepAliveSeconds(),
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(cacheConfigProperties.getAsyncExecutor().getQueueCapacity()),
                new NamedThreadFactory(cacheConfigProperties.getAsyncExecutor().getThreadNamePrefix()));
        poolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        return poolExecutor;
    }

    @Bean
    @ConditionalOnBean(RedissonClient.class)
    public CacheService redisCacheServiceImpl(RedissonClient redissonClient,
                                              RedisSendService redisSendService,
                                              ExecutorService redisExecutor,
                                              MultiLayerCacheProperties cacheConfigProperties) {
        CacheService cacheService = null;
        // 判断是否开启二级缓存
        if (cacheConfigProperties.isSecondCacheEnable()) {
            CacheService redisCacheService = new RedisCacheServiceImpl(redissonClient, redisExecutor);
            cacheService = new CaffeineCacheServiceImpl(redisCacheService, cacheConfigProperties, redisSendService);
        } else {
            cacheService = new RedisCacheServiceImpl(redissonClient, redisExecutor);
        }
        return cacheService;
    }

    @Bean
    @ConditionalOnBean(RedissonClient.class)
    public RedisSendService redisSendService(RedissonClient redissonClient, MultiLayerCacheProperties cacheConfigProperties) {
        return new RedisSendServiceImpl(redissonClient, cacheConfigProperties.getRemoteCache().getTopicName());
    }

    /**
     * 设置消息监听器
     */
    @Bean
    @ConditionalOnProperty(
            value = " multi.cache.secondCacheEnable",
            havingValue = "true")
    public RTopic subscribe(RedissonClient redissonClient, CacheService caffeineCacheService, MultiLayerCacheProperties cacheConfigProperties) {
        RTopic rTopic = redissonClient.getTopic(cacheConfigProperties.getRemoteCache().getTopicName());
        CacheMessageListener messageListener = new CacheMessageListener((CaffeineCacheServiceImpl) caffeineCacheService);
        rTopic.addListener(CacheData.class, messageListener);
        return rTopic;
    }
}
