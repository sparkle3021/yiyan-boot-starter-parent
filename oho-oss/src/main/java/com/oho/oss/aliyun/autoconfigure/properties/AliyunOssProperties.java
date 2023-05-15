package com.oho.oss.aliyun.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云OSS配置类
 *
 * @author MENGJIAO
 * @createDate 2023-04-27 9:23
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "oss.aliyun")
public class AliyunOssProperties {
    private boolean enable = false;

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String bucketName;
}
