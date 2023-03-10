package com.oho.common.utils.excel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Excel 表数据信息
 *
 * @author Sparkler
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SheetData {
    /**
     * 表名，非必须
     */
    private String sheetName;

    @NotNull
    private Class<?> clazz;

    @NotNull
    private List<?> sheetList;

    private ExcelCellMergeParam excelCellMergeParam;
}