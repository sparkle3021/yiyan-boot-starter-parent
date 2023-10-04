package com.yiyan.boot.redis.autoconfigure;


import com.yiyan.boot.common.utils.BeanUtils;
import com.yiyan.boot.common.utils.ObjectUtils;
import com.yiyan.boot.redis.autoconfigure.properties.RedissonProperties;
import com.yiyan.boot.redis.core.utils.RedisLockUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
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
 * @author MENGJIAO
 * @createDate 2023 /1/4
 */
@Slf4j
@ConditionalOnClass(Redisson.class)
@Configuration
@ConditionalOnProperty(value = "spring.redisson.enable", havingValue = "true")
@EnableConfigurationProperties({RedissonProperties.class})
@Import({RedisLockUtil.class})
public class RedissonAutoConfiguration {
    /**
     * Redisson redisson client.
     *
     * @param redissonProperties the redisson properties
     * @return the redisson client
     */
    @Bean(destroyMethod = "shutdown", name = "redissonClient")
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

}