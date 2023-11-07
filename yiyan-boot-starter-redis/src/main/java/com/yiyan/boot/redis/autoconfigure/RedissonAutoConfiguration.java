package com.yiyan.boot.redis.autoconfigure;


import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

/**
 * The type Redisson configuration.
 *
 * @author MENGJIAO
 * @createDate 2023 /1/4
 */
@Slf4j
@ConditionalOnClass(Redisson.class)
@Configuration
public class RedissonAutoConfiguration {

}