package com.yiyan.boot.redis.autoconfigure;


import com.yiyan.boot.redis.core.utils.RedisLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@Import({RedisLockUtil.class})
public class RedissonAutoConfiguration {

}