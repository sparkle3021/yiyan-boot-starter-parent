package com.oho.redis.autoconfigure.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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
}
