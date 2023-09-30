package com.yiyan.boot.oss.s3.autoconfigure;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.yiyan.boot.oss.s3.autoconfigure.properties.S3Properties;
import com.yiyan.boot.oss.s3.core.service.OssTemplate;
import com.yiyan.boot.oss.s3.core.service.impll.OssTemplateImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * OSS S3 API 自动配置
 *
 * @author MENGJIAO
 * @createDate 2023-10-01 上午 05:19
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(S3AutoConfiguration.class)
@ConditionalOnProperty(prefix = "oss.s3.enable", havingValue = "true")
public class S3AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AmazonS3 amazonS3Client(S3Properties s3Properties) {
        log.info("[OSS S3] - 初始化AmazonS3Client");
        log.info("[OSS S3] - [S3 配置信息]： {}", s3Properties);
        // 客户端配置
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setMaxConnections(s3Properties.getMaxConnections());
        // url以及region配置
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
                s3Properties.getEndpoint(), s3Properties.getRegion());
        // 密钥配置
        AWSCredentials awsCredentials = new BasicAWSCredentials(s3Properties.getAccessKey(),
                s3Properties.getSecretKey());
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
        // build amazonS3Client客户端
        AmazonS3 amazonS3 = AmazonS3Client.builder().withEndpointConfiguration(endpointConfiguration)
                .withClientConfiguration(clientConfiguration).withCredentials(awsCredentialsProvider)
                .disableChunkedEncoding().withPathStyleAccessEnabled(s3Properties.getPathStyleAccess()).build();
        log.info("[OSS S3] - 初始化AmazonS3Client完成");
        return amazonS3;
    }

    /**
     * 注册S3 Oss操作实现
     */
    @Bean
    @ConditionalOnBean(AmazonS3.class)
    public OssTemplate ossTemplate(AmazonS3 amazonS3) {
        return new OssTemplateImpl(amazonS3);
    }
}
