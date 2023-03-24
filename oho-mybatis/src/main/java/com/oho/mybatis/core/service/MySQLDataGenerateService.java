package com.oho.mybatis.core.service;

import com.oho.mybatis.model.generator.Columns;
import com.oho.mybatis.model.generator.Tables;
import com.oho.mybatis.model.generator.qo.ColumnInfoDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * MySQL 数据生成 Service
 *
 * @author MENGJIAO
 * @createDate 2023 /03/21
 */
public interface MySQLDataGenerateService {

    /**
     * 查询数据库所有表信息
     *
     * @return List<TablesDO>   list
     */
    List<Tables> selectAllTableInfo();

    /**
     * 查询表的所有字段信息
     *
     * @param tableName 表名
     * @return List<ColumnsDO>   list
     */
    List<Columns> selectColumnsByTableName(@Param("tableName") String tableName);

    /**
     * 生成随机数据
     *
     * @param dataType the data type
     * @param number   the number
     * @return object object
     */
    Object dataReturn(String dataType, Integer number);

    /**
     * 生成主键
     *
     * @param dataType the data type
     * @return object object
     */
    Object pkReturn(String dataType);

    /**
     * 单线程插入数据
     *
     * @param tableName   the table name
     * @param columnsInfo the column info
     * @param records     the records
     */
    void batchInsertData(String tableName, List<ColumnInfoDTO> columnsInfo, Integer records);

    /**
     * 多线程批量插入数据
     *
     * @param tableName   the table name
     * @param columnsInfo the columns info
     * @param records     the records
     * @param threadSize  the thread size
     */
    void toThreadBatchInsert(String tableName, List<ColumnInfoDTO> columnsInfo, Integer records, Integer... threadSize);
}
