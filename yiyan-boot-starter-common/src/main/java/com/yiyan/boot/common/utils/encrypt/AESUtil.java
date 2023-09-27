package com.yiyan.boot.common.utils.encrypt;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.AES;
import com.yiyan.boot.common.utils.StringUtils;

import java.io.InputStream;

/**
 * AES 加密算法工具类
 *
 * @author Sparkler
 * @createDate 2023 /1/4
 */
public class AESUtil {

    private AESUtil() {
    }

    private static final AES AES = new AES();

    /**
     * 生成密钥
     *
     * @return string
     */
    public static String generateSecretKey() {
        return MD5Util.md5Hex(RandomUtil.randomString(EncryptConstant.ASE_SECRET_LENGTH));
    }

    /**
     * AES 加密
     *
     * @param content 待加密字符串
     * @return string 加密后的Base64串
     */
    public static String encode(String content) {
        return AES.encryptBase64(content);
    }


    /**
     * AES 加密
     *
     * @param data 待加密数据
     * @return string 加密后的Base64串
     */
    public static String encode(InputStream data) {
        return AES.encryptBase64(data);
    }


    /**
     * AES 解密
     *
     * @param encodeStr 待解密数据
     * @return string 解密后的数据
     */
    public static String decode(String encodeStr) {
        return AES.decryptStr(encodeStr, CharsetUtil.CHARSET_UTF_8);
    }


    /**
     * AES 解密
     *
     * @param data 待解密数据
     * @return string 解密后的数据
     */
    public static String decode(InputStream data) {
        return AES.decryptStr(data);
    }

    // -------------------------------------------------------------------------------- 指定加密密钥

    /**
     * AES 加密
     *
     * @param secret  指定加密密钥，密钥长度为16位，不足自动补0
     * @param content 待加密字符
     * @return string 加密后的Base64串
     */
    public static String encode(String secret, String content) {
        return new AES(StrUtil.bytes(StringUtils.fillAfter(secret, EncryptConstant.FILL_CHAR, EncryptConstant.ASE_SECRET_LENGTH))).encryptBase64(content);
    }

    /**
     * AES 加密
     *
     * @param secret 指定加密密钥
     * @param data   待加密数据
     * @return string 加密后的Base64串
     */
    public static String encode(String secret, InputStream data) {
        return new AES(StrUtil.bytes(StringUtils.fillAfter(secret, EncryptConstant.FILL_CHAR, EncryptConstant.ASE_SECRET_LENGTH))).encryptBase64(data);
    }

    /**
     * AES 解密
     *
     * @param secret    指定加密密钥
     * @param encodeStr 待解密数据
     * @return string 解密后的数据
     */
    public static String decode(String secret, String encodeStr) {
        return new AES(StrUtil.bytes(StringUtils.fillAfter(secret, EncryptConstant.FILL_CHAR, EncryptConstant.ASE_SECRET_LENGTH))).decryptStr(encodeStr, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * AES 解密
     *
     * @param secret 指定加密密钥
     * @param data   待解密数据
     * @return string 解密后的数据
     */
    public static String decode(String secret, InputStream data) {
        return new AES(StrUtil.bytes(StringUtils.fillAfter(secret, EncryptConstant.FILL_CHAR, EncryptConstant.ASE_SECRET_LENGTH))).decryptStr(data);
    }
}
