package com.oho.common.utils.encrypt;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Bcrypt 加密工具类 .
 *
 * @author Sparkler
 * @createDate 2023 /2/11-18:05
 */
public class BCryptUtil {

    private BCryptUtil() {
    }

    /**
     * 加密
     *
     * @param context 原文
     * @return string
     */
    public static String encode(String context) {
        return BCrypt.hashpw(context, BCrypt.gensalt());
    }

    /**
     * 校验原文与密文是否匹配
     *
     * @param context   原文
     * @param encodeStr 密文
     * @return the boolean
     */
    public static Boolean matches(String context, String encodeStr) {
        return BCrypt.checkpw(context, encodeStr);
    }
}
