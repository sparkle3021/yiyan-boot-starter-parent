package com.yiyan.boot.oss.qiniu.core.enums;

/**
 * 七牛云 CDN查询粒度
 *
 * @author MENGJIAO
 * @createDate 2023 -04-27 16:07
 */
public enum GranularityEnum {
    /**
     * Min granularity enum.
     */
    MIN("5min"),
    /**
     * Hour granularity enum.
     */
    HOUR("hour"),
    /**
     * Day granularity enum.
     */
    DAY("day");

    private final String value;

    GranularityEnum(String value) {
        this.value = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }
}
