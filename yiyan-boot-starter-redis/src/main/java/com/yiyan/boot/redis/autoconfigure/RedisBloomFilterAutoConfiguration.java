package com.yiyan.boot.redis.autoconfigure;


import com.yiyan.boot.common.utils.CollectionUtils;
import com.yiyan.boot.common.utils.SpringContextUtils;
import com.yiyan.boot.redis.autoconfigure.properties.BloomFilterProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author MENGJIAO
 * @createDate 2023-09-24 下午 01:52
 */
@Slf4j
@ConditionalOnClass(Redisson.class)
@Configuration
@ConditionalOnProperty(value = "redis.bloom-filter.enable", havingValue = "true")
@EnableConfigurationProperties({BloomFilterProperties.class})
public class RedisBloomFilterAutoConfiguration {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 初始化配置文件中的布隆过滤器
     */
    @Bean("bloomFilter")
    public void buildBloomFilter(BloomFilterProperties bloomFilterProperties) {
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
