package com.yiyan.boot.common.utils.encrypt;

import cn.hutool.crypto.SecureUtil;

import java.io.File;
import java.io.InputStream;

/**
 * SHA 加密工具类
 *
 * @author Sparkler
 * @createDate 2023/1/4
 */
public class SHAUtil {
    private SHAUtil() {
    }

    // -------------------------------------------------------------------------------- SHA1加密

    /**
     * SHA1加密，生成16进制SHA1字符串<br>
     *
     * @param data 数据
     * @return SHA1字符串
     */
    public static String sha1Encode(String data) {
        return SecureUtil.sha1().digestHex(data);
    }

    /**
     * SHA1加密，生成16进制SHA1字符串<br>
     *
     * @param data 数据
     * @return SHA1字符串
     */
    public static String sha1Encode(InputStream data) {
        return SecureUtil.sha1().digestHex(data);
    }

    /**
     * SHA1加密文件，生成16进制SHA1字符串<br>
     *
     * @param dataFile 被加密文件
     * @return SHA1字符串
     */
    public static String sha1Encode(File dataFile) {
        return SecureUtil.sha1().digestHex(dataFile);
    }

    // -------------------------------------------------------------------------------- SHA256加密

    /**
     * SHA256加密，生成16进制SHA256字符串<br>
     *
     * @param data 数据
     * @return SHA256字符串
     * @since 4.3.2
     */
    public static String sha256Encode(String data) {
        return SecureUtil.sha256().digestHex(data);
    }

    /**
     * SHA256加密，生成16进制SHA256字符串<br>
     *
     * @param data 数据
     * @return SHA1字符串
     * @since 4.3.2
     */
    public static String sha256Encode(InputStream data) {
        return SecureUtil.sha256().digestHex(data);
    }

    /**
     * SHA256加密文件，生成16进制SHA256字符串<br>
     *
     * @param dataFile 被加密文件
     * @return SHA256字符串
     * @since 4.3.2
     */
    public static String sha256Encode(File dataFile) {
        return SecureUtil.sha256().digestHex(dataFile);
    }
}
