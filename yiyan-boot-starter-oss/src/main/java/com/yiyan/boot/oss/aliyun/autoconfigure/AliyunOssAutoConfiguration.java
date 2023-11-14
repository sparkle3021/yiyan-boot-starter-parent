package com.yiyan.boot.oss.aliyun.autoconfigure;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.yiyan.boot.oss.aliyun.autoconfigure.properties.AliyunOssProperties;
import com.yiyan.boot.oss.aliyun.core.AliyunOssUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 阿里云OSS自动配置类
 *
 * @author MENGJIAO
 * @createDate 2023-04-27 9:13
 */
@Configuration
@ConditionalOnClass(AliyunOssUtil.class)
@EnableConfigurationProperties(AliyunOssProperties.class)
@ConditionalOnProperty(prefix = "oss.aliyun.enable", havingValue = "true")
public class AliyunOssAutoConfiguration {
    /**
     * 注入属性配置类
     */
    @Resource
    private AliyunOssProperties aliyunOssProperties;

    /**
     * 注册OSS Client实例
     */
    @Bean
    @ConditionalOnProperty(prefix = "oss.aliyun", value = "enable", havingValue = "true")
    public OSS ossClient() {
        return new OSSClientBuilder().build(aliyunOssProperties.getEndpoint(),
                aliyunOssProperties.getAccessKeyId(), aliyunOssProperties.getAccessKeySecret());
    }

    /**
     * 注册Aliyun OSS操作工具类实例
     */
    @Bean
    public AliyunOssUtil aliyunOssUtil() {
        return new AliyunOssUtil();
    }
}
