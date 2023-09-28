package com.yiyan.boot.common.utils.excel.model;

import lombok.Data;

/**
 * @author Sparkler
 * @createDate 2022/12/9
 */

@Data
public class ExcelCellMergeParam {

    /**
     * 设置第几列合并
     */
    private int[] mergeColumnIndex;

    /**
     * 需要从第几行开始合并
     */
    private int mergeRowIndex = 1;
}
