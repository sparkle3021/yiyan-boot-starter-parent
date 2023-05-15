package com.oho.mybatis.core.model.constant;

/**
 * MySQL 数据类型
 *
 * @author Sparkler
 * @createDate 2022/11/15
 */
public class MySQLDataType {
    // 数值类型
    /**
     * 小整数值
     */
    public static final String TINYINT = "tinyint";

    /**
     * 大整数值
     */
    public static final String SMALLINT = "smallint";

    /**
     * 大整数值
     */
    public static final String MEDIUMINT = "mediumint";

    /**
     * 大整数值
     */
    public static final String INT = "int";

    /**
     * 大整数值
     */
    public static final String INTEGER = "integer";

    /**
     * 极大整数值
     */
    public static final String BIGINT = "bigint";

    /**
     * 单精度
     * 浮点数值
     */
    public static final String FLOAT = "float";

    /**
     * 双精度
     * 浮点数值
     */
    public static final String DOUBLE = "double";

    /**
     * 小数值
     */
    public static final String DECIMAL = "decimal";

    // 日期和时间类型
    /**
     * 日期值 YYYY-MM-DD
     */
    public static final String DATE = "date";

    /**
     * 时间值或持续时间 HH:MM:SS
     */
    public static final String TIME = "time";

    /**
     * 年份值 	YYYY
     */
    public static final String YEAR = "year";

    /**
     * 混合日期和时间值 YYYY-MM-DD hh:mm:ss
     */
    public static final String DATETIME = "datetime";

    /**
     * 混合日期和时间值，时间戳 YYYY-MM-DD hh:mm:ss
     */
    public static final String TIMESTAMP = "timestamp";

    // 字符串类型
    /**
     * 定长字符串
     */
    public static final String CHAR = "char";

    /**
     * 变长字符串
     */
    public static final String VARCHAR = "varchar";

    /**
     * 不超过 255 个字符的二进制字符串
     */
    public static final String TINYBLOB = "tinyblob";

    /**
     * 短文本字符串
     */
    public static final String TINYTEXT = "tinytext";

    /**
     * 二进制形式的长文本数据
     */
    public static final String BLOB = "blob";

    /**
     * 长文本数据
     */
    public static final String TEXT = "text";

    /**
     * 二进制形式的中等长度文本数据
     */
    public static final String MEDIUMBLOB = "mediumblob";

    /**
     * 中等长度文本数据
     */
    public static final String MEDIUMTEXT = "mediumtext";

    /**
     * 二进制形式的极大文本数据
     */
    public static final String LONGBLOB = "longblob";

    /**
     * 极大文本数据
     */
    public static final String LONGTEXT = "longtext";
}
