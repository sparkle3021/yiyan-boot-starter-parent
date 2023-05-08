package com.oho.redis.autoconfigure;

import com.oho.common.utils.BeanUtils;
import com.oho.common.utils.CollectionUtils;
import com.oho.common.utils.ObjectUtils;
import com.oho.common.utils.SpringContextUtils;
import com.oho.redis.autoconfigure.properties.BloomFilterProperties;
import com.oho.redis.autoconfigure.properties.RedissonProperties;
import com.oho.redis.core.utils.RedisLockUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The type Redisson configuration.
 *
 * @author Sparkler
 * @createDate 2023 /1/4
 */
@Slf4j
@ConditionalOnClass(Redisson.class)
@Configuration
@ConditionalOnProperty(value = "spring.redisson.enable", havingValue = "true")
@EnableConfigurationProperties({RedissonProperties.class, BloomFilterProperties.class})
@Import({RedisLockUtil.class})
public class RedissonAutoConfiguration {
    /**
     * Redisson redisson client.
     *
     * @param redissonProperties the redisson properties
     * @return the redisson client
     */
    @Bean(destroyMethod = "shutdown")
    RedissonClient redisson(RedissonProperties redissonProperties) {
        RedissonConfig redissonConfig = setServerConfig(redissonProperties);
        return Redisson.create(redissonConfig);
    }

    /**
     * The type Redisson config.
     */
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class RedissonConfig extends Config {
        @Override
        protected void setClusterServersConfig(ClusterServersConfig clusterServersConfig) {
            super.setClusterServersConfig(clusterServersConfig);
        }

        @Override
        protected void setReplicatedServersConfig(ReplicatedServersConfig replicatedServersConfig) {
            super.setReplicatedServersConfig(replicatedServersConfig);
        }

        @Override
        protected void setSingleServerConfig(SingleServerConfig singleConnectionConfig) {
            super.setSingleServerConfig(singleConnectionConfig);
        }

        @Override
        protected void setSentinelServersConfig(SentinelServersConfig sentinelConnectionConfig) {
            super.setSentinelServersConfig(sentinelConnectionConfig);
        }

        @Override
        protected void setMasterSlaveServersConfig(MasterSlaveServersConfig masterSlaveConnectionConfig) {
            super.setMasterSlaveServersConfig(masterSlaveConnectionConfig);
        }
    }

    /**
     * 填充具体配置项
     */
    private RedissonConfig setServerConfig(RedissonProperties redissonProperties) {
        RedissonConfig redissonConfig = new RedissonConfig();
        BeanUtils.copyProperties(redissonProperties, redissonConfig);
        // 单实例配置
        if (ObjectUtils.isNotNull(redissonProperties.getSingleServerConfig())) {
            SingleServerConfig singleServerConfig = redissonConfig.useSingleServer();
            BeanUtils.copyProperties(redissonProperties.getSingleServerConfig(), singleServerConfig);
            redissonConfig.setSingleServerConfig(singleServerConfig);
        }
        // 哨兵模式配置
        if (ObjectUtils.isNotNull(redissonProperties.getSentinelServersConfig())) {
            redissonConfig.setSentinelServersConfig(redissonProperties.getSentinelServersConfig());
        }
        // 主从模式配置
        if (ObjectUtils.isNotNull(redissonProperties.getMasterSlaveServersConfig())) {
            redissonConfig.setMasterSlaveServersConfig(redissonProperties.getMasterSlaveServersConfig());
        }
        // 集群模式配置
        if (ObjectUtils.isNotNull(redissonProperties.getClusterServersConfig())) {
            redissonConfig.setClusterServersConfig(redissonProperties.getClusterServersConfig());
        }
        // 托管模式配置
        if (ObjectUtils.isNotNull(redissonProperties.getReplicatedServersConfig())) {
            redissonConfig.setReplicatedServersConfig(redissonProperties.getReplicatedServersConfig());
        }
        return redissonConfig;
    }


    /**
     * 初始化配置文件中的布隆过滤器
     */
    @Bean
    public void buildBloomFilter(BloomFilterProperties bloomFilterProperties, RedissonClient redissonClient) {
        if (CollectionUtils.isEmpty(bloomFilterProperties.getProperties())) {
            log.info("布隆过滤器配置为空");
            return;
        }

        for (BloomFilterProperties.Properties property : bloomFilterProperties.getProperties()) {
            RBloomFilter<T> bloomFilter = initBloomFilter(property.getBloomFilterName(), property.getFalseProbability(), property.getExpectedElements(), redissonClient);
            SpringContextUtils.registerBean(property.getBloomFilterName(), bloomFilter);
            log.info("布隆过滤器[{}]初始化完成，误判率为[{}]，预计数据插入量为[{}]", property.getBloomFilterName(), property.getFalseProbability(), property.getExpectedElements());
        }
    }

    /**
     * 初始化布隆过滤器
     *
     * @param name     过滤器名称
     * @param fpp      误判率
     * @param capacity 预计插入元素数量
     */
    public RBloomFilter<T> initBloomFilter(String name, double fpp, long capacity, RedissonClient redissonClient) {
        // 通过RedissonClient获取布隆过滤器
        RBloomFilter<T> bloomFilter = redissonClient.getBloomFilter(name, new StringCodec());
        // 初始化布隆过滤器，需要指定预计插入元素数量和误判率
        bloomFilter.tryInit(capacity, fpp);
        return bloomFilter;
    }
}