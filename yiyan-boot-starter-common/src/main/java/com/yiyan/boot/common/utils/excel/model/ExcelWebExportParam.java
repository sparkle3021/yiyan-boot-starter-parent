package com.yiyan.boot.common.utils.excel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The type Excel web export param.
 *
 * @author Sparkler
 * @createDate 2022 /12/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelWebExportParam {
    /**
     * 导出时的文件名
     */
    private String fileName;

    /**
     * Sheet表数据
     */
    private List<SheetData> sheetDataList;

}
