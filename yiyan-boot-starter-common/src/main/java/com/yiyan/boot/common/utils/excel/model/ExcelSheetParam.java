package com.yiyan.boot.common.utils.excel.model;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Excel sheet param.
 *
 * @author Sparkler
 * @createDate 2022 /12/9
 */
@Getter
@Setter
public class ExcelSheetParam {

    /**
     * sheet 下标，从0开始
     */
    private int index;

    /**
     * sheet 数据类
     */
    private Class<?> clazz;

}
