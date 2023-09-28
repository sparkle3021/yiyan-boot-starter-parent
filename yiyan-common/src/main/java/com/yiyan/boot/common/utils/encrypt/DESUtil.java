package com.yiyan.boot.common.utils.encrypt;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.yiyan.boot.common.utils.StringUtils;

import java.io.InputStream;

/**
 * DES 加密工具类
 *
 * @author Sparkler
 * @createDate 2023 /1/4
 */
public class DESUtil {

    private DESUtil() {
    }


    private static final cn.hutool.crypto.symmetric.DES DES = SecureUtil.des();

    /**
     * 生成密钥
     *
     * @return string
     */
    public static String generateSecretKey() {
        return MD5Util.md5Hex(RandomUtil.randomString(EncryptConstant.DES_SECRET_LENGTH));
    }

    /**
     * DES 加密
     *
     * @param content 待加密字符串
     * @return string 加密后的Base64串
     */
    public static String encode(String content) {
        return DES.encryptBase64(content);
    }

    /**
     * DES 加密
     *
     * @param data 待加密数据
     * @return string 加密后的Base64串
     */
    public static String encode(InputStream data) {
        return DES.encryptBase64(data);
    }

    /**
     * DES 解密
     *
     * @param encodeStr 待解密数据
     * @return string 解密后的数据
     */
    public static String decode(String encodeStr) {
        return DES.decryptStr(encodeStr, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * DES 解密
     *
     * @param data 待解密数据
     * @return string 解密后的数据
     */
    public static String decode(InputStream data) {
        return DES.decryptStr(data);
    }

    // -------------------------------------------------------------------------------- 指定加密密钥

    /**
     * DES 加密
     *
     * @param secret  指定加密密钥
     * @param content 待加密字符
     * @return string 加密后的Base64串
     */
    public static String encode(String secret, String content) {
        return SecureUtil.des(StrUtil.bytes(secret)).encryptBase64(content);
    }

    /**
     * DES 加密
     *
     * @param secret 指定加密密钥
     * @param data   待加密数据
     * @return string 加密后的Base64串
     */
    public static String encode(String secret, InputStream data) {
        return SecureUtil.des(StrUtil.bytes(secret)).encryptBase64(data);
    }

    /**
     * DES 解密
     *
     * @param secret    指定加密密钥
     * @param encodeStr 待解密数据
     * @return string 解密后的数据
     */
    public static String decode(String secret, String encodeStr) {
        return SecureUtil.des(StringUtils.bytes(secret)).decryptStr(encodeStr, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * DES 解密
     *
     * @param secret 指定加密密钥
     * @param data   待解密数据
     * @return string 解密后的数据
     */
    public static String decode(String secret, InputStream data) {
        return SecureUtil.des(StrUtil.bytes(secret)).decryptStr(data);
    }
}
