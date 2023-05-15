package com.oho.payment.alipay.autoconfigure;

import com.oho.payment.alipay.core.config.AliPayConfig;
import com.oho.payment.alipay.core.utils.AlipayApiUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Alipay SDK自动配置
 *
 * @author Sparkler
 * @createDate 2023 /1/14
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "alipay", name = "appId")
@Import({AlipayApiUtil.class})
@EnableConfigurationProperties(AliPayConfig.class)
public class AlipayAutoConfiguration {

}