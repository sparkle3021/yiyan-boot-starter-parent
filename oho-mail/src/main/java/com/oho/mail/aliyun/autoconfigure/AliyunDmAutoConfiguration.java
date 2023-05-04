package com.oho.mail.aliyun.autoconfigure;

import com.oho.mail.aliyun.autoconfigure.properties.AliyunDmProperties;
import com.oho.mail.aliyun.core.AliyunDmUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 阿里云DM邮件配置
 *
 * @author MENGJIAO
 * @createDate 2023-04-27 17:23
 */
@Configuration
@ConditionalOnClass(AliyunDmUtils.class)
@EnableConfigurationProperties(AliyunDmProperties.class)
public class AliyunDmAutoConfiguration {
    /**
     * 注入属性配置类
     */
    @Resource
    private AliyunDmProperties aliyunDmProperties;


    /**
     * 注册Aliyun DM Client实例
     */
    @Bean
    @ConditionalOnProperty(prefix = "email.aliyun", value = "enable", havingValue = "true")
    public com.aliyun.dm20151123.Client ossClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(aliyunDmProperties.getAccessKeyId())
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(aliyunDmProperties.getAccessKeySecret());
        // 访问的域名
        config.endpoint = aliyunDmProperties.getEndpoint();
        return new com.aliyun.dm20151123.Client(config);
    }

    /**
     * 注册Aliyun DM操作工具类实例
     */
    @Bean
    public AliyunDmUtils aliyunDmUtils() {
        return new AliyunDmUtils();
    }
}
