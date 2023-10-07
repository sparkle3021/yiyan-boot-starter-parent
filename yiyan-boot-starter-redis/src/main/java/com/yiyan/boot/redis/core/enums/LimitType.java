package com.yiyan.boot.redis.core.enums;

import lombok.Getter;

/**
 * 窗口限流类型
 *
 * @author MENGJIAO
 * @createDate 2023-10-07 0007 下午 12:14
 */
@Getter
public enum LimitType {
    /**
     * 固定窗口限流
     */
    FIXED_WINDOW_RATE_LIMIT(0),
    /**
     * 滑动窗口限流
     */
    SLIDING_WINDOW_RATE_LIMIT(1);

    private final int type;

    LimitType(int type) {
        this.type = type;
    }
}
