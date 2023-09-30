package com.yiyan.boot.oss.s3.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * OSS S3 API 配置
 *
 * @author MENGJIAO
 * @createDate 2023-10-01 上午 05:14
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "oss.s3")
public class S3Properties {

    /**
     * 是否启用S3 API
     */
    private boolean enable = false;

    /**
     * 访问地址
     */
    private String endpoint;

    /**
     * 区域
     */
    private String region;

    /**
     * Access Key
     */
    private String accessKey;

    /**
     * Secret Key
     */
    private String secretKey;

    /**
     * 是否启用路径模式
     */
    private Boolean pathStyleAccess = true;

    /**
     * 最大连接数
     */
    private Integer maxConnections = 100;
}
