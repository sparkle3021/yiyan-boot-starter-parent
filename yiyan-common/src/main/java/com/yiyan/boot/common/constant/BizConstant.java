package com.yiyan.boot.common.constant;

/**
 * 业务常量
 *
 * @author Sparkler
 * @createDate 2023/2/11-21:50
 */
public class BizConstant {
    /**
     * 默认Token头
     */
    public static final String DEFAULT_TOKEN_HEADER = "Authorization";

    /**
     * 默认Token过期时间，七天
     */
    public static final int DEFAULT_TOKEN_EXPIRE = 604800;

    /**
     * 默认刷新Token过期时间，30天
     */
    public static final int DEFAULT_TOKEN_REFRESH_EXPIRE = 2592000;

    /**
     * 默认加密密钥
     */
    public static final String DEFAULT_SECRET = "is_app_default_secret";
}
