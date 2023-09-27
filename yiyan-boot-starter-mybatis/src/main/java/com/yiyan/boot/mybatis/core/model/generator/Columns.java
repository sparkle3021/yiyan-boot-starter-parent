package com.yiyan.boot.mybatis.core.model.generator;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName COLUMNS
 */
@TableName(value = "COLUMNS")
@Data
public class Columns implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private String tableCatalog;
    /**
     *
     */
    private String tableSchema;
    /**
     *
     */
    private String tableName;
    /**
     *
     */
    private String columnName;
    /**
     *
     */
    private Object ordinalPosition;
    /**
     *
     */
    private String columnDefault;
    /**
     *
     */
    private String isNullable;
    /**
     *
     */
    private String dataType;
    /**
     *
     */
    private Long characterMaximumLength;
    /**
     *
     */
    private Long characterOctetLength;
    /**
     *
     */
    private Long numericPrecision;
    /**
     *
     */
    private Long numericScale;
    /**
     *
     */
    private Object datetimePrecision;
    /**
     *
     */
    private String characterSetName;
    /**
     *
     */
    private String collationName;
    /**
     *
     */
    private String columnType;
    /**
     *
     */
    private Object columnKey;
    /**
     *
     */
    private String extra;
    /**
     *
     */
    private String privileges;
    /**
     *
     */
    private String columnComment;
    /**
     *
     */
    private String generationExpression;
    /**
     *
     */
    private Object srsId;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Columns other = (Columns) that;
        return (this.getTableCatalog() == null ? other.getTableCatalog() == null : this.getTableCatalog().equals(other.getTableCatalog()))
                && (this.getTableSchema() == null ? other.getTableSchema() == null : this.getTableSchema().equals(other.getTableSchema()))
                && (this.getTableName() == null ? other.getTableName() == null : this.getTableName().equals(other.getTableName()))
                && (this.getColumnName() == null ? other.getColumnName() == null : this.getColumnName().equals(other.getColumnName()))
                && (this.getOrdinalPosition() == null ? other.getOrdinalPosition() == null : this.getOrdinalPosition().equals(other.getOrdinalPosition()))
                && (this.getColumnDefault() == null ? other.getColumnDefault() == null : this.getColumnDefault().equals(other.getColumnDefault()))
                && (this.getIsNullable() == null ? other.getIsNullable() == null : this.getIsNullable().equals(other.getIsNullable()))
                && (this.getDataType() == null ? other.getDataType() == null : this.getDataType().equals(other.getDataType()))
                && (this.getCharacterMaximumLength() == null ? other.getCharacterMaximumLength() == null : this.getCharacterMaximumLength().equals(other.getCharacterMaximumLength()))
                && (this.getCharacterOctetLength() == null ? other.getCharacterOctetLength() == null : this.getCharacterOctetLength().equals(other.getCharacterOctetLength()))
                && (this.getNumericPrecision() == null ? other.getNumericPrecision() == null : this.getNumericPrecision().equals(other.getNumericPrecision()))
                && (this.getNumericScale() == null ? other.getNumericScale() == null : this.getNumericScale().equals(other.getNumericScale()))
                && (this.getDatetimePrecision() == null ? other.getDatetimePrecision() == null : this.getDatetimePrecision().equals(other.getDatetimePrecision()))
                && (this.getCharacterSetName() == null ? other.getCharacterSetName() == null : this.getCharacterSetName().equals(other.getCharacterSetName()))
                && (this.getCollationName() == null ? other.getCollationName() == null : this.getCollationName().equals(other.getCollationName()))
                && (this.getColumnType() == null ? other.getColumnType() == null : this.getColumnType().equals(other.getColumnType()))
                && (this.getColumnKey() == null ? other.getColumnKey() == null : this.getColumnKey().equals(other.getColumnKey()))
                && (this.getExtra() == null ? other.getExtra() == null : this.getExtra().equals(other.getExtra()))
                && (this.getPrivileges() == null ? other.getPrivileges() == null : this.getPrivileges().equals(other.getPrivileges()))
                && (this.getColumnComment() == null ? other.getColumnComment() == null : this.getColumnComment().equals(other.getColumnComment()))
                && (this.getGenerationExpression() == null ? other.getGenerationExpression() == null : this.getGenerationExpression().equals(other.getGenerationExpression()))
                && (this.getSrsId() == null ? other.getSrsId() == null : this.getSrsId().equals(other.getSrsId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTableCatalog() == null) ? 0 : getTableCatalog().hashCode());
        result = prime * result + ((getTableSchema() == null) ? 0 : getTableSchema().hashCode());
        result = prime * result + ((getTableName() == null) ? 0 : getTableName().hashCode());
        result = prime * result + ((getColumnName() == null) ? 0 : getColumnName().hashCode());
        result = prime * result + ((getOrdinalPosition() == null) ? 0 : getOrdinalPosition().hashCode());
        result = prime * result + ((getColumnDefault() == null) ? 0 : getColumnDefault().hashCode());
        result = prime * result + ((getIsNullable() == null) ? 0 : getIsNullable().hashCode());
        result = prime * result + ((getDataType() == null) ? 0 : getDataType().hashCode());
        result = prime * result + ((getCharacterMaximumLength() == null) ? 0 : getCharacterMaximumLength().hashCode());
        result = prime * result + ((getCharacterOctetLength() == null) ? 0 : getCharacterOctetLength().hashCode());
        result = prime * result + ((getNumericPrecision() == null) ? 0 : getNumericPrecision().hashCode());
        result = prime * result + ((getNumericScale() == null) ? 0 : getNumericScale().hashCode());
        result = prime * result + ((getDatetimePrecision() == null) ? 0 : getDatetimePrecision().hashCode());
        result = prime * result + ((getCharacterSetName() == null) ? 0 : getCharacterSetName().hashCode());
        result = prime * result + ((getCollationName() == null) ? 0 : getCollationName().hashCode());
        result = prime * result + ((getColumnType() == null) ? 0 : getColumnType().hashCode());
        result = prime * result + ((getColumnKey() == null) ? 0 : getColumnKey().hashCode());
        result = prime * result + ((getExtra() == null) ? 0 : getExtra().hashCode());
        result = prime * result + ((getPrivileges() == null) ? 0 : getPrivileges().hashCode());
        result = prime * result + ((getColumnComment() == null) ? 0 : getColumnComment().hashCode());
        result = prime * result + ((getGenerationExpression() == null) ? 0 : getGenerationExpression().hashCode());
        result = prime * result + ((getSrsId() == null) ? 0 : getSrsId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", tableCatalog=").append(tableCatalog);
        sb.append(", tableSchema=").append(tableSchema);
        sb.append(", tableName=").append(tableName);
        sb.append(", columnName=").append(columnName);
        sb.append(", ordinalPosition=").append(ordinalPosition);
        sb.append(", columnDefault=").append(columnDefault);
        sb.append(", isNullable=").append(isNullable);
        sb.append(", dataType=").append(dataType);
        sb.append(", characterMaximumLength=").append(characterMaximumLength);
        sb.append(", characterOctetLength=").append(characterOctetLength);
        sb.append(", numericPrecision=").append(numericPrecision);
        sb.append(", numericScale=").append(numericScale);
        sb.append(", datetimePrecision=").append(datetimePrecision);
        sb.append(", characterSetName=").append(characterSetName);
        sb.append(", collationName=").append(collationName);
        sb.append(", columnType=").append(columnType);
        sb.append(", columnKey=").append(columnKey);
        sb.append(", extra=").append(extra);
        sb.append(", privileges=").append(privileges);
        sb.append(", columnComment=").append(columnComment);
        sb.append(", generationExpression=").append(generationExpression);
        sb.append(", srsId=").append(srsId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}