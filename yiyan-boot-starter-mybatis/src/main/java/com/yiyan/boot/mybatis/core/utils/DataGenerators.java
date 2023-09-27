package com.yiyan.boot.mybatis.core.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.yiyan.boot.mybatis.core.model.constant.ConfigKey;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author Sparkler
 * @createDate 2022/11/15
 */
public class DataGenerators {


    /**
     * 雪花算法Id生成
     */
    private static final Snowflake snowflake = IdUtil.getSnowflake();

    /**
     * Id 生成（雪花算法）
     *
     * @return
     */
    public static long idNumberGenerator() {
        return snowflake.nextId();
    }

    /**
     * Id 生成（雪花算法）
     *
     * @return
     */
    public static String idStrGenerator() {
        return snowflake.nextIdStr();
    }

    /**
     * int 数据生成
     *
     * @param length 数据长度
     * @return int 数据值
     */
    public static int intDataGenerate(Integer length) {
        return RandomUtil.randomInt(ConfigKey.RANDOM_INT_MIN, ConfigKey.RANDOM_INT_MAX);
    }

    /**
     * long 数据生成
     *
     * @param length 数据长度
     * @return long 数据值
     */
    public static long longDataGenerate(Integer length) {
        return RandomUtil.randomLong(ConfigKey.RANDOM_LONG_MIN, ConfigKey.RANDOM_LONG_MAX);
    }

    /**
     * char 数据生成
     *
     * @param baseString 可选数据
     * @return long 数据值
     */
    public static char charDataGenerate(String baseString) {
        if (StrUtil.isNotBlank(baseString)) {
            return RandomUtil.randomChar(baseString);
        }
        return RandomUtil.randomChar();
    }

    /**
     * String 数据生成
     *
     * @param length 数据长度
     * @return long 数据值
     */
    public static String stringDataGenerate(Integer length) {
        return RandomUtil.randomString(length);
    }

    /**
     * Decimal 数据生成
     *
     * @param number 小数位
     * @return
     */
    public static BigDecimal decimalDataGenerate(Integer number) {
        if (ObjectUtil.isNull(number)) {
            number = ConfigKey.DEFAULT_DECIMAL_SCALE;
        }
        return BigDecimal.valueOf(RandomUtil.randomDouble(ConfigKey.RANDOM_INT_MIN, ConfigKey.RANDOM_INT_MAX)).setScale(number, ConfigKey.DECIMAL_ROUNDING_MODE);
    }

    /**
     * Date 数据生成
     *
     * @return date 数据值
     */
    public static Date dateDataGenerate() {
        return RandomUtil.randomDay(ConfigKey.RANDOM_DATE_MIN, ConfigKey.RANDOM_DATE_MAX);
    }

    /**
     * Byte[] 数据生成
     *
     * @param length 数据长度
     * @return byte[] 数据值
     */
    public static byte[] byteDataGenerate(Integer length) {
        return RandomUtil.randomBytes(length);
    }

    /**
     * Time 数据生成
     *
     * @return
     */
    public static LocalTime timeDataGenerate() {
        LocalDateTime localDateTime = Instant.ofEpochMilli(dateDataGenerate().getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime.toLocalTime();
    }

}
