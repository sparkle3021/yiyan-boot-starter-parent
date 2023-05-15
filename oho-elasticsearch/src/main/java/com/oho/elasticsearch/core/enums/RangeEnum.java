package com.oho.elasticsearch.core.enums;

/**
 * The enum Es range enum.
 *
 * @author Sparkler
 * @createDate 2023 /1/9
 */
public enum RangeEnum {
    /**
     * 起始，且包含起始值
     */
    FROM,
    /**
     * 起始，不包含包含起始值
     */
    R_FROM,
    /**
     * 包含结束值
     */
    TO,
    /**
     * 不包含结束值
     */
    L_TO,
    /**
     * 小于
     */
    LT,
    /**
     * 小于等于
     */
    LTE,
    /**
     * 大于
     */
    GT,
    /**
     * 大于等于
     */
    GTE
}
