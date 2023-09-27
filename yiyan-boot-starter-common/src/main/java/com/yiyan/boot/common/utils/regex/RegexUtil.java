package com.yiyan.boot.common.utils.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 * <p>
 * 内置方法为单次匹配，如果需要多次匹配，可使用{@link #isMatch(String, String)}方法
 *
 * @author MENGJIAO
 * @createDate 2023-04-25 22:00
 */
public class RegexUtil {

    private RegexUtil() {
    }

    /**
     * 单次匹配
     */
    public static boolean isMatchOnce(String regex, String value) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    /**
     * 多次匹配
     */
    public static boolean isMatch(String regex, String value) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.find();
    }

    /**
     * 匹配整数
     */
    public static boolean isInt(String value) {
        return isMatchOnce(RegexConstants.NUMBER, value);
    }

    /**
     * 匹配浮点数
     */
    public static boolean isDouble(String value) {
        return isMatchOnce(RegexConstants.DOUBLE, value);
    }

    /**
     * 匹配手机号码
     */
    public static boolean isMobile(String value) {
        return isMatchOnce(RegexConstants.MOBILE, value);
    }

    /**
     * 匹配邮箱
     */
    public static boolean isEmail(String value) {
        return isMatchOnce(RegexConstants.EMAIL, value);
    }

    /**
     * 匹配IPv4地址
     */
    public static boolean isIpV4(String value) {
        return isMatchOnce(RegexConstants.IPV4_ADDRESS, value);
    }

    /**
     * 匹配IPV6地址
     */
    public static boolean isIpv6(String value) {
        return isMatchOnce(RegexConstants.IPV6_ADDRESS, value);
    }

    /**
     * 匹配URL地址
     */
    public static boolean isUrl(String value) {
        return isMatchOnce(RegexConstants.URL, value);
    }

    /**
     * 匹配中国大陆身份证号码
     */
    public static boolean isIdCard(String value) {
        return isMatchOnce(RegexConstants.ID_CARD_CN, value);
    }

    /**
     * 匹配中文
     */
    public static boolean isChinese(String value) {
        return isMatchOnce(RegexConstants.CHINESE, value);
    }

    /**
     * 匹配英文
     */
    public static boolean isEnglish(String value) {
        return isMatchOnce(RegexConstants.ENGLISH, value);
    }

    /**
     * 匹配日期格式：yyyy-MM-dd
     */
    public static boolean isDate(String value) {
        return isMatchOnce(RegexConstants.DATE, value);
    }

    /**
     * 匹配日期格式：yyyy-MM-dd HH:mm:ss
     */
    public static boolean isDateTime(String value) {
        return isMatchOnce(RegexConstants.DATE_TIME, value);
    }

    /**
     * 匹配时间格式：HH:mm:ss
     */
    public static boolean isTime(String value) {
        return isMatchOnce(RegexConstants.TIME, value);
    }

    /**
     * 匹配中国邮政编码
     */
    public static boolean isPostCode(String value) {
        return isMatchOnce(RegexConstants.POST_CODE, value);
    }

    /**
     * 匹配中国车牌号码
     */
    public static boolean isPlateNumber(String value) {
        return isMatchOnce(RegexConstants.PLATE_NUMBER, value);
    }

    /**
     * 匹配QQ号码
     */
    public static boolean isQQ(String value) {
        return isMatchOnce(RegexConstants.QQ, value);
    }

}
