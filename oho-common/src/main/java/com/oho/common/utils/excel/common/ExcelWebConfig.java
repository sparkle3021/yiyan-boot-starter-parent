package com.oho.common.utils.excel.common;

import java.nio.charset.StandardCharsets;

/**
 * Excel web 导出配置
 *
 * @author Sparkler
 * @createDate 2022 /12/9
 */
public class ExcelWebConfig {

    private ExcelWebConfig() {
    }

    /**
     * 设置文件下载格式为 .xlsx
     */
    public static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    /**
     * 设置编码为 UTF-8
     */
    public static final String CHARACTER_ENCODING = String.valueOf(StandardCharsets.UTF_8);

    /**
     * 下载头
     */
    public static final String DOWNLOAD_HEADER = "Content-disposition";

    /**
     * 下载头内容文件名
     */
    public static final String DOWNLOAD_HEADER_VALUE = "attachment;filename=%s.xlsx";
}
