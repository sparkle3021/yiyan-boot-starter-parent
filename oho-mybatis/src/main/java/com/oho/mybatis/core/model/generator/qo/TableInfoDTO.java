package com.oho.mybatis.core.model.generator.qo;

import lombok.Data;

import java.util.List;

/**
 * @author Sparkler
 * @createDate 2022/11/12
 */
@Data
public class TableInfoDTO {
    /**
     * 表名
     */
    private String tableName;

    /**
     * 数据量
     */
    private Integer records;
    /**
     * 列
     */
    private List<ColumnInfoDTO> columns;
}
