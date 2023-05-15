package com.oho.common.utils.excel.core;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.oho.common.exception.Asserts;
import com.oho.common.exception.BizException;
import com.oho.common.utils.CollectionUtils;
import com.oho.common.utils.excel.common.ExcelFillCellMergeStrategy;
import com.oho.common.utils.excel.common.ExcelWebConfig;
import com.oho.common.utils.excel.model.ExcelCellMergeParam;
import com.oho.common.utils.excel.model.ExcelWebExportParam;
import com.oho.common.utils.excel.model.SheetData;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

/**
 * Excel 写入工具类
 *
 * @author Sparkler
 * @createDate 2022 /12/9
 */
public class ExcelWrite {

    private static final String SHEET_DEFAULT_NAME = "Sheet";

    /**
     * Excel 写入，适用于小数据量写入
     *
     * @param fileName  保存文件名，包括路径
     * @param sheetName sheet名
     * @param clazz     写入类型
     * @param data      待写入数据
     */
    public static void write(String fileName, String sheetName, Class<?> clazz, List data) {
        try (ExcelWriter excelWriter = EasyExcel.write(fileName, clazz).build()) {
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();
            excelWriter.write(data, writeSheet);
        }
    }

    /**
     * Web excel export.
     *
     * @param response    the response
     * @param exportParam the export param
     */
    public static void webExcelExport(HttpServletResponse response, ExcelWebExportParam exportParam) {
        webResponseSetting(response, exportParam);
        ExcelWriter writer = null;
        try {
            int sheetIndex = 0;
            writer = EasyExcel.write(response.getOutputStream()).autoCloseStream(Boolean.FALSE).build();
            List<SheetData> sheetDataList = exportParam.getSheetDataList();

            if (CollectionUtils.isEmpty(sheetDataList)) {
                throw new BizException("Sheet Data 为空");
            }

            for (SheetData sheetData : sheetDataList) {
                ExcelCellMergeParam excelCellMergeParam = sheetData.getExcelCellMergeParam();
                // 判断是否需要合并单元行
                boolean needCellMerge = !Objects.isNull(excelCellMergeParam);
                String sheetName = StringUtils.isBlank(sheetData.getSheetName()) ? SHEET_DEFAULT_NAME.concat(String.valueOf(sheetIndex + 1)) : sheetData.getSheetName();
                WriteSheet sheet;
                if (sheetIndex == 0) {
                    writer = EasyExcel.write(response.getOutputStream(), sheetData.getClazz()).build();
                    if (needCellMerge) {
                        sheet = EasyExcel.writerSheet(sheetIndex++, sheetName)
                                .registerWriteHandler(new ExcelFillCellMergeStrategy(excelCellMergeParam.getMergeRowIndex(), excelCellMergeParam.getMergeColumnIndex())).build();
                    } else {
                        sheet = EasyExcel.writerSheet(sheetIndex++, sheetName).build();
                    }
                    writer.write(sheetData.getSheetList(), sheet);
                } else {
                    if (needCellMerge) {
                        sheet = EasyExcel.writerSheet(sheetIndex++, sheetName)
                                .registerWriteHandler(new ExcelFillCellMergeStrategy(excelCellMergeParam.getMergeRowIndex(), excelCellMergeParam.getMergeColumnIndex()))
                                .head(sheetData.getClazz()).build();
                    } else {
                        sheet = EasyExcel.writerSheet(sheetIndex++, sheetName).build();
                    }
                    writer.write(sheetData.getSheetList(), sheet);
                }
            }
        } catch (IOException e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            e.printStackTrace();
            Asserts.fail(e.getMessage());
        } finally {
            assert writer != null;
            writer.finish();
        }
    }

    /**
     * Web 参数设置
     *
     * @param response
     * @param exportParam
     */
    private static void webResponseSetting(HttpServletResponse response, ExcelWebExportParam exportParam) {
        response.setContentType(ExcelWebConfig.CONTENT_TYPE);
        response.setCharacterEncoding(ExcelWebConfig.CHARACTER_ENCODING);
        String fileName = StringUtils.isBlank(exportParam.getFileName()) ? String.valueOf(System.currentTimeMillis()) : exportParam.getFileName();
        fileName = URLEncoder.encode(fileName).replaceAll("\\+", "%20");
        response.setHeader(ExcelWebConfig.DOWNLOAD_HEADER, String.format(ExcelWebConfig.DOWNLOAD_HEADER_VALUE, fileName));
    }
}
