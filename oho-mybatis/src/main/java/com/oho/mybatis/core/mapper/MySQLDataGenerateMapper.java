package com.oho.mybatis.core.mapper;

import com.oho.mybatis.model.generator.Columns;
import com.oho.mybatis.model.generator.Tables;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * MySQL 数据生成 Mapper
 *
 * @author Sparkler
 * @createDate 2022 /11/12
 */
@Mapper
public interface MySQLDataGenerateMapper {

    /**
     * 查询数据库所有表信息
     *
     * @return List<TablesDO> list
     */
    List<Tables> selectAllTableInfo();

    /**
     * 查询表的所有字段信息
     *
     * @param tableName 表名
     * @return List<ColumnsDO> list
     */
    List<Columns> selectColumnsByTableName(@Param("tableName") String tableName);

    /**
     * 插入数据
     *
     * @param tableName the table name
     * @param columns   the columns
     * @param data      the data
     */
    void insertData(@Param("tableName") String tableName, @Param("columns") List<String> columns, @Param("data") List<Map<String, Object>> data);

}
