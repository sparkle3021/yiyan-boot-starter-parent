package com.yiyan.boot.oss.qiniu.autoconfigure;

import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import com.yiyan.boot.oss.qiniu.autoconfigure.properties.QiniuOssProperties;
import com.yiyan.boot.oss.qiniu.core.QiniuOssUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 七牛云OSS自动配置类
 *
 * @author MENGJIAO
 * @createDate 2023-04-27 13:17
 */
@Configuration
@ConditionalOnClass(QiniuOssUtil.class)
@EnableConfigurationProperties(QiniuOssProperties.class)
@ConditionalOnProperty(prefix = "oss.aliyun.enable", havingValue = "true")
public class QiniuOssAutoConfiguration {
    /**
     * 注入属性配置类
     */
    @Resource
    private QiniuOssProperties qiniuOssProperties;

    /**
     * 注册七牛Auth实例
     */
    @Bean
    @ConditionalOnProperty(prefix = "oss.qiniu", value = "enable", havingValue = "true")
    public Auth auth() {
        return Auth.create(qiniuOssProperties.getAccessKey(), qiniuOssProperties.getSecretKey());
    }

    /**
     * 注册Qiniu OSS操作工具类实例
     */
    @Bean
    public QiniuOssUtil aliyunOssUtil() {
        return new QiniuOssUtil();
    }

    @Bean
    public com.qiniu.storage.Configuration configuration() {
        com.qiniu.storage.Configuration configuration = new com.qiniu.storage.Configuration(Region.autoRegion());
        // 指定分片上传版本
        configuration.resumableUploadAPIVersion = com.qiniu.storage.Configuration.ResumableUploadAPIVersion.V2;
        return configuration;
    }

    /**
     * 构造七牛云OOS Region 配置类
     */
    @Bean
    public UploadManager uploadManager() throws IOException {
        //设置断点续传文件进度保存目录
        FileRecorder fileRecorder = new FileRecorder(qiniuOssProperties.getLocalTempDir());
        return new UploadManager(configuration(), fileRecorder);
    }


}
