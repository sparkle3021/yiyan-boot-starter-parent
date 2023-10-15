package com.yiyan.boot.common.enums;

/**
 * 排序枚举
 *
 * @author MENGJIAO
 * @createDate 2023-10-15 0015 下午 01:32
 */
public enum QueryOrderEnum {
    ASC("ASC"),
    DESC("DESC");
    private final String value;

    QueryOrderEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
