package com.yiyan.boot.common.utils.encrypt;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * MD5加密工具类
 *
 * @author Sparkler
 * @createDate 2023 /1/4
 */
public class MD5Util {

    private MD5Util() {
    }

    private static final MD5 MD5 = new MD5();

    /**
     * MD5加密 Hex
     *
     * @param content 待加密数据
     * @return string 加密后Hex数据
     */
    public static String md5Hex(String content) {
        return MD5.digestHex16(content);
    }

    /**
     * MD5加密 Hex
     *
     * @param content 待加密数据
     * @param charset 编码格式
     * @return string 加密后Hex数据
     */
    public static String md5Hex(String content, Charset charset) {
        return MD5.digestHex16(content, charset);
    }

    /**
     * MD5加密 Hex
     *
     * @param file 待加密数据
     * @return string 加密后Hex数据
     */
    public static String md5Hex(File file) {
        return MD5.digestHex16(file);
    }

    /**
     * MD5加密 Hex
     *
     * @param data 待加密数据
     * @return string 加密后Hex数据
     */
    public static String md5Hex(byte[] data) {
        return MD5.digestHex16(data);
    }

    /**
     * MD5加密 Hex
     *
     * @param data 待加密数据
     * @return string 加密后Hex数据
     */
    public static String md5Hex(InputStream data) {
        return MD5.digestHex16(data);
    }


    // -------------------------------------------------------------------------------- 加盐

    /**
     * MD5加密 Hex
     *
     * @param salt    盐值
     * @param content 待加密数据
     * @return string 加密后Hex数据
     */
    public static String md5Hex(String salt, String content) {
        return new MD5(StrUtil.bytes(salt)).digestHex16(content);
    }

    /**
     * MD5加密 Hex
     *
     * @param salt    盐值
     * @param content 待加密数据
     * @param charset 编码格式
     * @return string 加密后Hex数据
     */
    public static String md5Hex(String salt, String content, Charset charset) {
        return new MD5(StrUtil.bytes(salt)).digestHex16(content, charset);
    }

    /**
     * MD5加密 Hex
     *
     * @param salt 盐值
     * @param file 待加密数据
     * @return string 加密后Hex数据
     */
    public static String md5Hex(String salt, File file) {
        return new MD5(StrUtil.bytes(salt)).digestHex16(file);
    }

    /**
     * MD5加密 Hex
     *
     * @param salt 盐值
     * @param data 待加密数据
     * @return string 加密后Hex数据
     */
    public static String md5Hex(String salt, byte[] data) {
        return new MD5(StrUtil.bytes(salt)).digestHex16(data);
    }

    /**
     * MD5加密 Hex
     *
     * @param salt 盐值
     * @param data 待加密数据
     * @return string 加密后Hex数据
     */
    public static String md5Hex(String salt, InputStream data) {
        return new MD5(StrUtil.bytes(salt)).digestHex16(data);
    }

}
