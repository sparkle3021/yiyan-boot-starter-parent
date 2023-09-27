package com.yiyan.boot.common.utils.fakevalue.model.enums;


import com.yiyan.boot.common.utils.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据库数据类型-Java数据类型枚举
 *
 * @author Sparkler
 * @createDate 2022 /12/5
 */
public enum FieldTypeEnum {

    /**
     * Tinyint field type enum.
     */
    TINYINT("tinyint", "Integer"),
    /**
     * Smallint field type enum.
     */
    SMALLINT("smallint", "Integer"),
    /**
     * Mediumint field type enum.
     */
    MEDIUMINT("mediumint", "Integer"),
    /**
     * Int field type enum.
     */
    INT("int", "Integer"),
    /**
     * Bigint field type enum.
     */
    BIGINT("bigint", "Long"),
    /**
     * Float field type enum.
     */
    FLOAT("float", "Double"),
    /**
     * Double field type enum.
     */
    DOUBLE("double", "Double"),
    /**
     * Decimal field type enum.
     */
    DECIMAL("decimal", "BigDecimal"),
    /**
     * Date field type enum.
     */
    DATE("date", "Date"),
    /**
     * Time field type enum.
     */
    TIME("time", "Time"),
    /**
     * Year field type enum.
     */
    YEAR("year", "Integer"),
    /**
     * Datetime field type enum.
     */
    DATETIME("datetime", "Date"),
    /**
     * Timestamp field type enum.
     */
    TIMESTAMP("timestamp", "Long"),
    /**
     * Char field type enum.
     */
    CHAR("char", "String"),
    /**
     * Varchar field type enum.
     */
    VARCHAR("varchar", "String"),
    /**
     * Tinytext field type enum.
     */
    TINYTEXT("tinytext", "String"),
    /**
     * Text field type enum.
     */
    TEXT("text", "String"),
    /**
     * Mediumtext field type enum.
     */
    MEDIUMTEXT("mediumtext", "String"),
    /**
     * Longtext field type enum.
     */
    LONGTEXT("longtext", "String"),
    /**
     * Tinyblob field type enum.
     */
    TINYBLOB("tinyblob", "byte[]"),
    /**
     * Blob field type enum.
     */
    BLOB("blob", "byte[]"),
    /**
     * Mediumblob field type enum.
     */
    MEDIUMBLOB("mediumblob", "byte[]"),
    /**
     * Longblob field type enum.
     */
    LONGBLOB("longblob", "byte[]"),
    /**
     * Binary field type enum.
     */
    BINARY("binary", "byte[]"),
    /**
     * Varbinary field type enum.
     */
    VARBINARY("varbinary", "byte[]");

    private final String dbType;

    private final String javaType;

    FieldTypeEnum(String dbType, String javaType) {
        this.dbType = dbType;
        this.javaType = javaType;
    }

    /**
     * 获取值列表
     *
     * @return db types
     */
    public static List<String> getDbTypes() {
        return Arrays.stream(values()).map(FieldTypeEnum::getDbType).collect(Collectors.toList());
    }

    /**
     * 根据 dbType 获取枚举
     *
     * @param dbType the db type
     * @return enum by db type
     */
    public static FieldTypeEnum getEnumByDbType(String dbType) {
        if (StringUtils.isBlank(dbType)) {
            return null;
        }
        for (FieldTypeEnum mockTypeEnum : FieldTypeEnum.values()) {
            if (mockTypeEnum.dbType.equals(dbType)) {
                return mockTypeEnum;
            }
        }
        return null;
    }

    /**
     * Gets db type.
     *
     * @return the db type
     */
    public String getDbType() {
        return dbType;
    }

    /**
     * Gets java type.
     *
     * @return the java type
     */
    public String getJavaType() {
        return javaType;
    }
}
