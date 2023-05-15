package com.oho.common.utils.regex;

/**
 * 正则表达式常量
 *
 * @author MENGJIAO
 * @createDate 2023-04-25 22:03
 */
public class RegexConstants {
    /**
     * 数字
     */
    public static final String NUMBER = "^\\d+$";
    /**
     * 浮点数
     */
    public static final String DOUBLE = "^(-?\\d+)(\\.\\d+)?$";
    /**
     * 手机号码
     */
    public static final String MOBILE = "^1[3-9]\\d{9}$";
    /**
     * 邮箱
     */
    public static final String EMAIL = "[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+";
    /**
     * 中国大陆身份证号码
     */
    public static final String ID_CARD_CN = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
    /**
     * URL
     */
    public static final String URL = "(https://|http://)?([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?";
    /**
     * IPv4地址
     */
    public static final String IPV4_ADDRESS = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    /**
     * IPv6地址
     */
    public static final String IPV6_ADDRESS = "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$";
    /**
     * 中文
     */
    public static final String CHINESE = "^[\u4e00-\u9fa5]+$";
    /**
     * 英文
     */
    public static final String ENGLISH = "^[A-Za-z]+$";

    /**
     * 日期格式
     */
    public static final String DATE = "^\\d{4}-\\d{1,2}-\\d{1,2}$";
    /**
     * 时间格式
     */
    public static final String TIME = "^([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$";
    /**
     * 日期时间格式
     */
    public static final String DATE_TIME = "^\\d{4}-\\d{1,2}-\\d{1,2}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2}$";
    /**
     * 中国邮政编码
     */
    public static final String POST_CODE = "[1-9]\\d{5}(?!\\d)";
    /**
     * 中国车牌号码
     */
    public static final String PLATE_NUMBER = "[\\u4e00-\\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}";
    /**
     * QQ
     */
    public static final String QQ = "[1-9][0-9]{4,}";
}
