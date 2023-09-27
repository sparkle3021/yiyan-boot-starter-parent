package com.yiyan.boot.payment.alipay.core.config;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Sparkler
 * @createDate 2023/1/14
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "alipay")
public class AliPayConfig {

    private String protocol = "https";

    /**
     * 支付服务网关
     */
    private String gatewayHost;

    /**
     * 加密模式
     */
    private String signType = "RSA2";

    /**
     * 请填写您的AppId，例如：2019091767145019
     */
    private String appId;

    /**
     * 私钥证书路径
     */
    private String merchantCertPath;

    /**
     * 支付宝密钥路径
     */
    private String alipayCertPath;

    /**
     * 证书根路径
     */
    private String alipayRootCertPath;


    /**
     * 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
     * 请填写您的应用私钥，例如：MIIEvQIBADANB ... ...
     */
    private String merchantPrivateKey;

    /**
     * 注：如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
     * 请填写您的支付宝公钥，例如：MIIBIjANBg...
     */
    private String alipayPublicKey;

    /**
     * 可设置AES密钥，调用AES加解密相关接口时需要（可选）
     * 请填写您的AES密钥，例如：aa4BtZ4tspm2wnXLb1ThQA==
     */
    private String encryptKey;

    /**
     * 回调地址
     */
    private String notifyUrl;

    @PostConstruct
    public void init() {
        // 设置参数（全局只需设置一次）
        Config config = new Config();
        config.protocol = this.protocol;
        config.gatewayHost = this.gatewayHost;
        config.signType = this.signType;
        config.appId = this.appId;
        // 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
        config.merchantPrivateKey = this.merchantPrivateKey;
        //注：证书文件路径支持设置为文件系统中的路径或CLASS_PATH中的路径，优先从文件系统中加载，加载失败后会继续尝试从CLASS_PATH中加载
        config.merchantCertPath = this.merchantCertPath;
        config.alipayCertPath = this.alipayCertPath;
        config.alipayRootCertPath = this.alipayRootCertPath;
        //注：如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
        config.alipayPublicKey = this.alipayPublicKey;
        //可设置异步通知接收服务地址（可选）
        config.notifyUrl = this.notifyUrl;
        //可设置AES密钥，调用AES加解密相关接口时需要（可选）
        config.encryptKey = this.encryptKey;

        Factory.setOptions(config);
        log.info("============= Alipay SDK 初始化成功 =============");
    }


}