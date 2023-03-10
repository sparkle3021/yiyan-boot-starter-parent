package com.oho.common.utils.excel.common;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * The type Excel common.
 *
 * @author Sparkler
 * @createDate 2022 /12/9
 */
public class ExcelCommon {

    /**
     * 获取Excel 所有Sheet信息
     *
     * @param fileName the file name
     * @return list
     */
    public static List<ReadSheet> hasSheets(String fileName) {
        try (ExcelReader build = EasyExcel.read(fileName).build()) {
            return build.excelExecutor().sheetList();
        }
    }

    /**
     * 获取Excel 所有Sheet信息
     *
     * @param file the file
     * @return list
     */
    public static List<ReadSheet> hasSheets(MultipartFile file) throws IOException {
        try (ExcelReader build = EasyExcel.read(file.getInputStream()).build()) {
            return build.excelExecutor().sheetList();
        }
    }
}
