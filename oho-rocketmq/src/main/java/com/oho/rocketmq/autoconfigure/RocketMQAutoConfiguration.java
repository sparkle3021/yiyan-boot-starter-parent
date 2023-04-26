package com.oho.rocketmq.autoconfigure;

import com.oho.rocketmq.core.utils.RocketMQUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author MENGJIAO
 */
@Slf4j
@Import({RocketMQUtil.class})
@Configuration
@ConditionalOnProperty(value = "rocketmq.name-server")
@ComponentScan("com.oho.rocketmq")
public class RocketMQAutoConfiguration {

}
