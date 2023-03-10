package com.oho.common.constant;

/**
 * @author Sparkler
 * @createDate 2023/2/11-21:40
 */
public class RedisCacheKeyConstant {

    private RedisCacheKeyConstant() {
    }

    /**
     * Redis 项目根Key
     */
    private static final String BASE_KEY = "OHO:";

    /**
     * 用户业务根Key
     */
    private static final String USER_KEY = BASE_KEY + "user:";

    /**
     * Token Key：1 username
     */
    public static final String USER_TOKEN_KEY = USER_KEY + "auth:%s:token";

    /**
     * Token 刷新令牌Key：1 username
     */
    public static final String USER_REFRESH_TOKEN_KEY = USER_KEY + "auth:%s:refresh_token";

    /**
     * 当前用户信息Key：1. username
     */
    public static final String USER_CURRENT_KEY = USER_KEY + "%s:current";
}
