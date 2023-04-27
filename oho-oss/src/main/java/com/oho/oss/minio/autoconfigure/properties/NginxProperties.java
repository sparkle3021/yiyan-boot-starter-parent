package com.oho.oss.minio.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * nginx配置
 *
 * @author Sparkler
 * @createDate 2021/6/25 15:42
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "oss.minio.nginx")
public class NginxProperties {

    /**
     * ip
     */
    private String endpoint;

    /**
     * 端口
     */
    private int port;

    /**
     * 协议
     * http或者https
     */
    private String protocol;

    /**
     * 请求前置url
     */
    private String prefixNginxUrl;

    @PostConstruct
    public void init() {
        this.setPrefixNginxUrl(getProtocol() + "://" + getEndpoint() + ":" + getPort() + "/");
    }

}
