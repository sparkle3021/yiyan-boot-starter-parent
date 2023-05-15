package com.oho.redis.autoconfigure.properties;

import com.oho.common.utils.CollectionUtils;
import com.oho.common.utils.SpringContextUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 布隆过滤器的配置属性
 *
 * @author MENGJIAO
 * @createDate 2023-05-08 10:37
 */
@Data
@Slf4j
@ConfigurationProperties(prefix = "redis.bloom-filter")
@Configuration
public class BloomFilterProperties {

    private List<Properties> properties;

    @Data
    public static class Properties {
        /**
         * 布隆过滤器的名称前缀
         */
        private String bloomFilterName;
        /**
         * 默认布隆过滤器的预计元素个数
         */
        private int expectedElements = 1000000;
        /**
         * 默认的布隆过滤器误差率
         */
        private double falseProbability = 0.01;
    }

    /**
     * 初始化配置文件中的布隆过滤器
     */
    @Autowired
    private BloomFilterProperties bloomFilterProperties;
    @Autowired
    private RedissonClient redissonClient;

    @PostConstruct
    public void buildBloomFilter() {
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
