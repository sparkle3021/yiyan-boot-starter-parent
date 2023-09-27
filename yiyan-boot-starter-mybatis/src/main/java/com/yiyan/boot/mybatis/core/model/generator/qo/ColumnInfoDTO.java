package com.yiyan.boot.mybatis.core.model.generator.qo;


import lombok.Data;

/**
 * @author Sparkler
 * @createDate 2022/11/12
 */

@Data
public class ColumnInfoDTO {
    /**
     * 列名
     */
    private String columnName;

    /**
     * 是否为主键 0，1;
     */
    private Integer primaryKey;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 数据长度
     */
    private Integer dataLength;

    /**
     * 逐渐策略，如果为自增则在生产数据时忽略该列。
     */
    private String extra;
}
