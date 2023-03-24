package com.oho.mybatis.test;

import com.oho.common.enums.YesNoEnum;
import com.oho.common.utils.ObjectUtils;
import com.oho.mybatis.core.service.MySQLDataGenerateService;
import com.oho.mybatis.model.generator.Columns;
import com.oho.mybatis.model.generator.Tables;
import com.oho.mybatis.model.generator.qo.ColumnInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MENGJIAO
 */
@RestController
@Slf4j
public class TestController {

    @Autowired
    private MySQLDataGenerateService mySQLDataGenerateService;

    @GetMapping("/tables")
    public List<Tables> getTableInfo() {
        return mySQLDataGenerateService.selectAllTableInfo();
    }

    @GetMapping("/columns")
    public List<Columns> getColumnsInfo(String tableName) {
        return mySQLDataGenerateService.selectColumnsByTableName(tableName);
    }

    /**
     * 单线程插入
     */
    @PostMapping("/batchInsert")
    public void batchInsert() {
        String tableName = "test_table";
        List<ColumnInfoDTO> columnInfoDTOList = getColumnInfoDTOS(tableName);
        mySQLDataGenerateService.batchInsertData(tableName, columnInfoDTOList, 5);
    }

    /**
     * 异步多线程插入
     */
    @PostMapping("/batchInsert2")
    public void batchInsert2() {
        String tableName = "test_table";
        List<ColumnInfoDTO> columnInfoDTOList = getColumnInfoDTOS(tableName);
        mySQLDataGenerateService.toThreadBatchInsert(tableName, columnInfoDTOList, 500123);
    }

    private List<ColumnInfoDTO> getColumnInfoDTOS(String tableName) {
        List<Columns> columns = mySQLDataGenerateService.selectColumnsByTableName(tableName);
        List<ColumnInfoDTO> columnInfoDTOList = new ArrayList<>(columns.size());
        for (Columns column : columns) {
            ColumnInfoDTO columnInfoDTO = new ColumnInfoDTO();
            columnInfoDTO.setColumnName(column.getColumnName());
            if ("id".equals(column.getColumnName())) {
                columnInfoDTO.setPrimaryKey(YesNoEnum.YES.getKey());
            } else {
                columnInfoDTO.setPrimaryKey(YesNoEnum.NO.getKey());
            }
            columnInfoDTO.setDataType(column.getDataType());
            if (ObjectUtils.isNotNull(column.getCharacterMaximumLength())) {
                columnInfoDTO.setDataLength(column.getCharacterMaximumLength().intValue());
            }
            columnInfoDTOList.add(columnInfoDTO);
        }
        return columnInfoDTOList;
    }
}
