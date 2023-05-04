package com.oho.mail.aliyun.core.enums;

/**
 * 阿里云DM地址类型枚举
 *
 * @author MENGJIAO
 * @createDate 2023-05-04 17:09
 */
public enum AddressTypeEnum {
    /**
     * 0：为随机账号
     */
    RANDOM(0),
    /**
     * 1：为发信地址
     */
    SEND(1);

    private final Integer value;

    AddressTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}
