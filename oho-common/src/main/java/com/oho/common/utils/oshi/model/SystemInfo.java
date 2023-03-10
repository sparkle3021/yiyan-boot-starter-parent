package com.oho.common.utils.oshi.model;

import lombok.Data;

import java.util.List;

/**
 * @author Sparkler
 * @createDate 2022/12/10
 */
@Data
public class SystemInfo {

    /**
     * 操作系统信息
     */
    private OsInfo osInfo;

    /**
     * Cpu信息
     */
    private CpusInfo cpusInfo;

    /**
     * 内存信息
     */
    private MemoryInfo memoryInfo;

    /**
     * 磁盘信息
     */
    private List<DiskInfo> diskInfos;
}
