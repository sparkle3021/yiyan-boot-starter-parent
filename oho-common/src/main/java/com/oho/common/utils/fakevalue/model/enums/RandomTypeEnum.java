package com.oho.common.utils.fakevalue.model.enums;

import com.oho.common.utils.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The enum Random type enum.
 *
 * @author Sparkler
 * @createDate 2022 /12/5
 */
public enum RandomTypeEnum {
    /**
     * String random type enum.
     */
    STRING("字符串"),
    /**
     * Name random type enum.
     */
    NAME("人名"),
    /**
     * City random type enum.
     */
    CITY("城市"),
    /**
     * Url random type enum.
     */
    URL("网址"),
    /**
     * Email random type enum.
     */
    EMAIL("邮箱"),
    /**
     * Ip random type enum.
     */
    IP("IP"),
    /**
     * Integer random type enum.
     */
    INTEGER("整数"),
    /**
     * Decimal random type enum.
     */
    DECIMAL("小数"),
    /**
     * University random type enum.
     */
    UNIVERSITY("大学"),
    /**
     * Date random type enum.
     */
    DATE("日期"),
    /**
     * Birthday random type enum.
     */
    BIRTHDAY("生日日期"),
    /**
     * Timestamp random type enum.
     */
    TIMESTAMP("时间戳"),
    /**
     * Address random type enum.
     */
    ADDRESS("地址"),
    /**
     * Phone random type enum.
     */
    PHONE("手机号"),
    /**
     * Id random type enum.
     */
    ID("唯一ID"),
    /**
     * Uuid random type enum.
     */
    UUID("UUID"),
    /**
     * 年龄
     */
    AGE("年龄"),
    /**
     * 食物
     */
    FOOD("食物"),
    /**
     * 工作职位
     */
    JOB("工作职位"),
    /**
     * 颜色
     */
    COLOR("颜色"),
    /**
     * 颜色 十六进制
     */
    COLOR_HEX("颜色（HEX）"),
    /**
     * 身份证号码
     */
    ID_CARD("身份证号码"),
    /**
     * 条形码
     */
    BARCODE("条形码"),
    /**
     * 药品
     */
    MEDICINE("药品"),
    /**
     * 疾病名
     */
    DISEASE("疾病名"),
    /**
     * 疾病临床症状
     */
    SYMPTOMS("疾病临床症状"),
    /**
     * 医院名
     */
    HOSPITAL("医院名");

    private final String value;

    RandomTypeEnum(String value) {
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return values values
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(RandomTypeEnum::getValue).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value the value
     * @return enum by value
     */
    public static RandomTypeEnum getEnumByValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        for (RandomTypeEnum randomTypeEnum : RandomTypeEnum.values()) {
            if (randomTypeEnum.value.equals(value)) {
                return randomTypeEnum;
            }
        }
        return null;
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
