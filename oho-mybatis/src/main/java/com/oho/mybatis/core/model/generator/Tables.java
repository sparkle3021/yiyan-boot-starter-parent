package com.oho.mybatis.core.model.generator;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName TABLES
 */
@TableName(value = "TABLES")
@Data
public class Tables implements Serializable {
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
    private Object tableType;
    /**
     *
     */
    private String engine;
    /**
     *
     */
    private Integer version;
    /**
     *
     */
    private Object rowFormat;
    /**
     *
     */
    private Long tableRows;
    /**
     *
     */
    private Long avgRowLength;
    /**
     *
     */
    private Long dataLength;
    /**
     *
     */
    private Long maxDataLength;
    /**
     *
     */
    private Long indexLength;
    /**
     *
     */
    private Long dataFree;
    /**
     *
     */
    private Long autoIncrement;
    /**
     *
     */
    private Date createTime;
    /**
     *
     */
    private Date updateTime;
    /**
     *
     */
    private Date checkTime;
    /**
     *
     */
    private String tableCollation;
    /**
     *
     */
    private Long checksum;
    /**
     *
     */
    private String createOptions;
    /**
     *
     */
    private String tableComment;

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
        Tables other = (Tables) that;
        return (this.getTableCatalog() == null ? other.getTableCatalog() == null : this.getTableCatalog().equals(other.getTableCatalog()))
                && (this.getTableSchema() == null ? other.getTableSchema() == null : this.getTableSchema().equals(other.getTableSchema()))
                && (this.getTableName() == null ? other.getTableName() == null : this.getTableName().equals(other.getTableName()))
                && (this.getTableType() == null ? other.getTableType() == null : this.getTableType().equals(other.getTableType()))
                && (this.getEngine() == null ? other.getEngine() == null : this.getEngine().equals(other.getEngine()))
                && (this.getVersion() == null ? other.getVersion() == null : this.getVersion().equals(other.getVersion()))
                && (this.getRowFormat() == null ? other.getRowFormat() == null : this.getRowFormat().equals(other.getRowFormat()))
                && (this.getTableRows() == null ? other.getTableRows() == null : this.getTableRows().equals(other.getTableRows()))
                && (this.getAvgRowLength() == null ? other.getAvgRowLength() == null : this.getAvgRowLength().equals(other.getAvgRowLength()))
                && (this.getDataLength() == null ? other.getDataLength() == null : this.getDataLength().equals(other.getDataLength()))
                && (this.getMaxDataLength() == null ? other.getMaxDataLength() == null : this.getMaxDataLength().equals(other.getMaxDataLength()))
                && (this.getIndexLength() == null ? other.getIndexLength() == null : this.getIndexLength().equals(other.getIndexLength()))
                && (this.getDataFree() == null ? other.getDataFree() == null : this.getDataFree().equals(other.getDataFree()))
                && (this.getAutoIncrement() == null ? other.getAutoIncrement() == null : this.getAutoIncrement().equals(other.getAutoIncrement()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getCheckTime() == null ? other.getCheckTime() == null : this.getCheckTime().equals(other.getCheckTime()))
                && (this.getTableCollation() == null ? other.getTableCollation() == null : this.getTableCollation().equals(other.getTableCollation()))
                && (this.getChecksum() == null ? other.getChecksum() == null : this.getChecksum().equals(other.getChecksum()))
                && (this.getCreateOptions() == null ? other.getCreateOptions() == null : this.getCreateOptions().equals(other.getCreateOptions()))
                && (this.getTableComment() == null ? other.getTableComment() == null : this.getTableComment().equals(other.getTableComment()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTableCatalog() == null) ? 0 : getTableCatalog().hashCode());
        result = prime * result + ((getTableSchema() == null) ? 0 : getTableSchema().hashCode());
        result = prime * result + ((getTableName() == null) ? 0 : getTableName().hashCode());
        result = prime * result + ((getTableType() == null) ? 0 : getTableType().hashCode());
        result = prime * result + ((getEngine() == null) ? 0 : getEngine().hashCode());
        result = prime * result + ((getVersion() == null) ? 0 : getVersion().hashCode());
        result = prime * result + ((getRowFormat() == null) ? 0 : getRowFormat().hashCode());
        result = prime * result + ((getTableRows() == null) ? 0 : getTableRows().hashCode());
        result = prime * result + ((getAvgRowLength() == null) ? 0 : getAvgRowLength().hashCode());
        result = prime * result + ((getDataLength() == null) ? 0 : getDataLength().hashCode());
        result = prime * result + ((getMaxDataLength() == null) ? 0 : getMaxDataLength().hashCode());
        result = prime * result + ((getIndexLength() == null) ? 0 : getIndexLength().hashCode());
        result = prime * result + ((getDataFree() == null) ? 0 : getDataFree().hashCode());
        result = prime * result + ((getAutoIncrement() == null) ? 0 : getAutoIncrement().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getCheckTime() == null) ? 0 : getCheckTime().hashCode());
        result = prime * result + ((getTableCollation() == null) ? 0 : getTableCollation().hashCode());
        result = prime * result + ((getChecksum() == null) ? 0 : getChecksum().hashCode());
        result = prime * result + ((getCreateOptions() == null) ? 0 : getCreateOptions().hashCode());
        result = prime * result + ((getTableComment() == null) ? 0 : getTableComment().hashCode());
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
        sb.append(", tableType=").append(tableType);
        sb.append(", engine=").append(engine);
        sb.append(", version=").append(version);
        sb.append(", rowFormat=").append(rowFormat);
        sb.append(", tableRows=").append(tableRows);
        sb.append(", avgRowLength=").append(avgRowLength);
        sb.append(", dataLength=").append(dataLength);
        sb.append(", maxDataLength=").append(maxDataLength);
        sb.append(", indexLength=").append(indexLength);
        sb.append(", dataFree=").append(dataFree);
        sb.append(", autoIncrement=").append(autoIncrement);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", checkTime=").append(checkTime);
        sb.append(", tableCollation=").append(tableCollation);
        sb.append(", checksum=").append(checksum);
        sb.append(", createOptions=").append(createOptions);
        sb.append(", tableComment=").append(tableComment);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}