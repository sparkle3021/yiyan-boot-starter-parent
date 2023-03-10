package com.oho.common.utils.encrypt;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

import java.security.KeyPair;

/**
 * RAS 加密工具类
 *
 * @author Sparkler
 * @createDate 2023/1/4
 */
public class RASUtil {

    private RASUtil() {
    }

    /**
     * 生成Ras密钥对
     *
     * @return rsa key pair
     */
    public static RsaKeyPair generateRasKeyPair() {
        KeyPair pair = SecureUtil.generateKeyPair("RSA");
        return new RsaKeyPair(Base64.encode(pair.getPublic().getEncoded()), Base64.encode(pair.getPrivate().getEncoded()));
    }

    /**
     * Ras 公钥加密
     *
     * @param <T>       the type parameter
     * @param publicKey the public key
     * @param content   the content
     * @return the string
     */
    public static <T> String rasEncode(String publicKey, String content) {
        RSA rsa = new RSA(null, publicKey);
        byte[] encrypt = rsa.encrypt(StrUtil.bytes(content, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
        return Base64.encode(encrypt);
    }

    /**
     * Ras 私钥解密
     *
     * @param privateKey the private key
     * @param encodeStr  the encode str
     * @return the string
     */
    public static String rasDecode(String privateKey, String encodeStr) {
        RSA rsa = new RSA(privateKey, null);
        byte[] decrypt = rsa.decrypt(Base64.decode(encodeStr), KeyType.PrivateKey);
        return StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8);
    }
}
