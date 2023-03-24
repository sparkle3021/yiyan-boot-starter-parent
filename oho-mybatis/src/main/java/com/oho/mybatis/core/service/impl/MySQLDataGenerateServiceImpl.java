package com.oho.mybatis.core.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.oho.common.enums.YesNoEnum;
import com.oho.mybatis.core.mapper.MySQLDataGenerateMapper;
import com.oho.mybatis.core.service.MySQLDataGenerateService;
import com.oho.mybatis.core.utils.DataGenerators;
import com.oho.mybatis.model.constant.DateFormatPattern;
import com.oho.mybatis.model.generator.qo.ColumnInfoDTO;
import com.oho.mybatis.model.generator.qo.TableInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import static com.oho.mybatis.model.constant.MySQLDataType.*;

@Slf4j
@Service
public class MySQLDataGenerateServiceImpl implements MySQLDataGenerateService {

    @Autowired
    private MySQLDataGenerateMapper mySQLDataGenerateMapper;

    @Override
    public Object dataReturn(String dataType, Integer length) {
        Object data = null;
        switch (dataType) {
            case TINYINT:
            case SMALLINT:
            case MEDIUMINT:
            case INT:
            case INTEGER:
                data = DataGenerators.intDataGenerate(length);
                break;
            case BIGINT:
                data = DataGenerators.longDataGenerate(length);
                break;
            case FLOAT:
            case DOUBLE:
            case DECIMAL:
                data = DataGenerators.decimalDataGenerate(length);
                break;
            case CHAR:
                data = DataGenerators.charDataGenerate(null);
                break;
            case VARCHAR:
            case TINYTEXT:
            case TEXT:
            case MEDIUMTEXT:
            case LONGTEXT:
                data = DataGenerators.stringDataGenerate(length);
                break;
            case TINYBLOB:
            case MEDIUMBLOB:
            case BLOB:
            case LONGBLOB:
                data = DataGenerators.byteDataGenerate(length);
                break;
            case DATE:
            case DATETIME:
                data = DataGenerators.dateDataGenerate();
                break;
            case TIME:
                data = DataGenerators.timeDataGenerate();
                break;
            case YEAR:
                data = DateUtil.format(DataGenerators.dateDataGenerate(), DateFormatPattern.YEAR);
                break;
            default:
                break;
        }
        return data;
    }

    @Override
    public Object pkReturn(String dataType) {
        Object data = null;
        switch (dataType) {
            case INT:
            case BIGINT:
                data = DataGenerators.idNumberGenerator();
                break;
            case VARCHAR:
                data = DataGenerators.idStrGenerator();
                break;
            default:
                break;
        }
        return data;
    }

    @Override
    public void batchInsertData(String tableName, List<ColumnInfoDTO> columnsInfo, Integer records) {
        dataGenerate(tableName, columnsInfo, records);
    }

    @Async
    @Override
    public void toThreadBatchInsert(TableInfoDTO tableInfoDTO, List<ColumnInfoDTO> columnsInfo, int threadSize, int onceInsertRecode) {
        CountDownLatch countDownLatch = new CountDownLatch(threadSize);
        for (int i = 0; i < threadSize; i++) {
            try {
                this.dataGenerate(tableInfoDTO.getTableName(), columnsInfo, onceInsertRecode);
            } finally {
                countDownLatch.countDown();
            }
        }
        try {
            countDownLatch.await();
        } catch (Exception e) {
            log.error("阻塞异常:" + e.getMessage());
        }
    }


    private void dataGenerate(String tableName, List<ColumnInfoDTO> columnsInfo, Integer records) {
        List<String> columnName = columnsInfo.stream().map(ColumnInfoDTO::getColumnName).collect(Collectors.toList());
        JSONArray jsonArray = new JSONArray(records);
        for (int i = 0; i < records; i++) {
            JSONObject jsonObject = new JSONObject();
            for (ColumnInfoDTO column : columnsInfo) {
                if (YesNoEnum.YES.getKey().equals(column.getPrimaryKey())) {
                    jsonObject.set(column.getColumnName(), pkReturn(column.getDataType()));
                } else {
                    jsonObject.set(column.getColumnName(), dataReturn(column.getDataType(), column.getDataLength()));
                }
            }
            jsonArray.add(jsonObject);
        }

        List data = new ArrayList<>();
        if (jsonArray.size() > 0) {
            data = jsonArray;
        }
        mySQLDataGenerateMapper.insertData(tableName, columnName, data);
    }
}
