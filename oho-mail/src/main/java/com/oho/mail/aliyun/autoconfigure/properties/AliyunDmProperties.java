package com.oho.mail.aliyun.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云DM邮件配置
 *
 * @author MENGJIAO
 * @createDate 2023-04-27 17:23
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "email.aliyun")
public class AliyunDmProperties {
    private Boolean enable = false;
    /**
     * 必填，服务的 AccessKey ID
     */
    private String accessKeyId;
    /**
     * 必填，服务的 AccessKey Secret
     */
    private String accessKeySecret;
    /**
     * 访问的域名
     */
    private String endpoint = "dm.aliyuncs.com";
}
