package com.yiyan.boot.redis.core.enums;

import lombok.Getter;

/**
 * 限流级别
 *
 * @author MENGJIAO
 * @createDate 2023-10-07 0007 上午 11:39
 */
@Getter
public enum LimitLevel {
    /**
     * 方法级别
     */
    METHOD(0),
    /**
     * IP级别
     */
    IP(1);

    private final int level;

    LimitLevel(int level) {
        this.level = level;
    }

}
