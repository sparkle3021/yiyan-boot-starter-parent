package com.yiyan.boot.rocketmq.autoconfigure;

import com.yiyan.boot.rocketmq.core.utils.RocketMQUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author MENGJIAO
 */
@Slf4j
@Import({RocketMQUtil.class})
@Configuration
@ConditionalOnProperty(value = "rocketmq.name-server")
public class RocketMQAutoConfiguration {

}
