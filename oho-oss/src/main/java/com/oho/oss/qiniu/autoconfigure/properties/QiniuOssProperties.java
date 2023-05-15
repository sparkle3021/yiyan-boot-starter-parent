package com.oho.oss.qiniu.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author MENGJIAO
 * @createDate 2023-04-27 13:18
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "oss.qiniu")
public class QiniuOssProperties {

    private boolean enable = false;

    private String accessKey;

    private String secretKey;

    private String bucketName;

    /**
     * 下载domain, eg: qiniu.com【必须】
     */
    private String domain;
    /**
     * 下载是否使用 https【必须】
     */
    private boolean useHttps = false;

    private String localTempDir = System.getProperty("java.io.tmpdir") + "/qiniu/";
}
