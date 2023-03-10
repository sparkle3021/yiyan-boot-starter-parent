package com.oho.common.utils.excel.core;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.oho.common.utils.excel.common.ExcelCommon;
import com.oho.common.utils.excel.core.listener.CommonListener;
import com.oho.common.utils.excel.model.ExcelSheetData;
import com.oho.common.utils.excel.model.ExcelSheetParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Excel读取工具类
 *
 * @param <T> the type parameter
 * @author Sparkler
 * @createDate 2022 /12/9
 */
@Slf4j
public class ExcelRead<T> {

    /**
     * 读取Excel
     *
     * @param <T>      数据实例类型
     * @param fileName 文件路径
     * @param clazz    实例类
     * @return excel数据
     */
    public static <T> List<T> read(String fileName, Class<T> clazz) {
        List<T> excelData = new ArrayList<>();
        List<ReadSheet> readSheets = ExcelCommon.hasSheets(fileName);
        for (ReadSheet readSheet : readSheets) {
            EasyExcel.read(fileName, clazz, new PageReadListener<T>(excelData::addAll))
                    .sheet()
                    .sheetNo(readSheet.getSheetNo())
                    .doRead();
        }
        return excelData;
    }

    /**
     * 读取Excel--多sheet
     *
     * @param fileName 文件路径
     * @param params   excel参数
     * @return excel数据
     */
    public static List<ExcelSheetData> read(String fileName, ExcelSheetParam... params) {
        List<ExcelSheetParam> excelSheetParams = Arrays.asList(params);
        if (excelSheetParams.size() == 0) {
            return null;
        }
        List<ExcelSheetData> excelSheets = new ArrayList<>();

        for (ExcelSheetParam param : excelSheetParams) {
            List sheetData = new ArrayList<>();
            EasyExcel.read(fileName)
                    .sheet()
                    .sheetNo(param.getIndex())
                    .head(param.getClazz())
                    .registerReadListener(new CommonListener<>(sheetData::addAll)).doRead();
            excelSheets.add(new ExcelSheetData(param.getClass(), sheetData));
        }

        return excelSheets;
    }


    /**
     * 读取上传的Excel
     *
     * @param <T>   the type parameter
     * @param file  the file
     * @param clazz the clazz
     * @return excel数据
     */
    public static <T> List<T> read(MultipartFile file, Class<T> clazz) throws IOException {
        List<T> excelData = new ArrayList<>();
        List<ReadSheet> readSheets = ExcelCommon.hasSheets(file);
        for (ReadSheet readSheet : readSheets) {
            EasyExcel.read(file.getInputStream(), clazz, new CommonListener<T>(excelData::addAll))
                    .sheet()
                    .sheetNo(readSheet.getSheetNo())
                    .doRead();
        }
        return excelData;
    }

    /**
     * 读取上传的Excel--多sheet
     *
     * @param file   上传的文件
     * @param params excel参数
     * @return excel数据
     */
    public static List<ExcelSheetData> read(MultipartFile file, ExcelSheetParam... params) throws IOException {
        List<ExcelSheetParam> excelSheetParams = Arrays.asList(params);
        if (excelSheetParams.size() == 0) {
            return null;
        }
        List<ExcelSheetData> excelSheets = new ArrayList<>();
        for (ExcelSheetParam param : excelSheetParams) {
            List sheetData = new ArrayList<>();
            EasyExcel.read(file.getInputStream())
                    .sheet()
                    .sheetNo(param.getIndex())
                    .head(param.getClazz())
                    .registerReadListener(new CommonListener<>(sheetData::addAll)).doRead();
            excelSheets.add(new ExcelSheetData(param.getClass(), sheetData));
        }
        return excelSheets;
    }
}
