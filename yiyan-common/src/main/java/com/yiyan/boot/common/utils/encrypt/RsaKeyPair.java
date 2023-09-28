package com.yiyan.boot.common.utils.encrypt;

import lombok.Data;

/**
 * 密钥对
 *
 * @author Sparkler
 * @createDate 2023/1/4
 */
@Data
public class RsaKeyPair {
    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 私钥
     */
    private String privateKey;

    public RsaKeyPair(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }
}
