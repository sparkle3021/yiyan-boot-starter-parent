package com.oho.common.utils.excel.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Sparkler
 * @createDate 2022/12/9
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExcelSheetData {
    private Class<?> clazz;

    private List<?> data;
}
