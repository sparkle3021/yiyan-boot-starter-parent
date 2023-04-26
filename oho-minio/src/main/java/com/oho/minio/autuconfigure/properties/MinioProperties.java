package com.oho.minio.autuconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * minio配置类
 *
 * @author Sparkler
 */
@Data
@ConfigurationProperties(prefix = "minio")
@Configuration
public class MinioProperties {
    /**
     * ip
     */
    private String endpoint = "127.0.0.1";

    /**
     * 端口
     */
    private int port = 9000;

    /**
     * 账号
     */
    private String accessKey;

    /**
     * 秘钥
     */
    private String secretKey;

    /**
     * 如果是true，则用的是https而不是http,默认值是true
     */
    private Boolean secure;

    /**
     * 桶名称，默认为 minio_oss_bucket
     */
    private String bucketName = "minio_oss_bucket";

    /**
     * 是否开启nginx路由
     */
    private Boolean nginxLoadUrlEnable = true;

    /**
     * 预览的url在nginx中的前缀
     */
    private String nginxLoadUrl;

    /**
     * 默认最大文件上传为500M
     */
    private Integer maxUploadFileSize = 1024 * 1024 * 500;
}