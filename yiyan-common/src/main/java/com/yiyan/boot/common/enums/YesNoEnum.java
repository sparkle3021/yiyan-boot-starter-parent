package com.yiyan.boot.common.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 是否判断枚举
 *
 * @author baiyan
 * @date 2021 /02/05
 */
public enum YesNoEnum {

    /**
     * Yes
     */
    YES(1, "是", Boolean.TRUE),
    /**
     * No
     */
    NO(0, "否", Boolean.FALSE),
    ;

    @Getter
    private Integer key;
    @Getter
    private String name;
    @Getter
    private Boolean value;

    YesNoEnum(Integer key, String name, Boolean value) {
        this.key = key;
        this.name = name;
        this.value = value;
    }

    /**
     * 根据枚举KEY获取枚举
     *
     * @param key the key
     * @return by key
     */
    public static YesNoEnum getByKey(Integer key) {
        return Arrays.stream(values()).filter(e -> Objects.equals(key, e.key)).findFirst().orElse(null);
    }

}
