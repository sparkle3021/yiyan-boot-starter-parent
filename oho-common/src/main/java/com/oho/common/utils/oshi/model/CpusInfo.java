package com.oho.common.utils.oshi.model;

import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.CpuTicks;
import lombok.Data;
import lombok.NoArgsConstructor;
import oshi.hardware.CentralProcessor;

/**
 * @author Sparkler
 * @createDate 2022/12/10
 */
@Data
@NoArgsConstructor
public class CpusInfo {

    /**
     * CPU数量
     */
    private Integer cpuNum;

    /**
     * 物理核心数
     */
    private Integer cpuPhysicalCores;

    /**
     * 逻辑核心数
     */
    private Integer cpuLogicalCores;

    /**
     * CPU系统使用率
     */
    private double sysRate;

    /**
     * CPU用户使用率
     */
    private double userRate;

    /**
     * CPU当前空闲率
     */
    private double freeRate;

    /**
     * CPU型号信息
     */
    private String cpuModel;

    /**
     * CPU型号信息
     */
    private CpuTicks ticks;

    public CpusInfo(CentralProcessor processor, CpuInfo cpuInfo) {
        init(processor, cpuInfo);
    }

    public void init(CentralProcessor processor, CpuInfo cpuInfo) {
        this.cpuNum = processor.getSystemCpuLoadTicks().length;
        this.cpuPhysicalCores = processor.getPhysicalProcessorCount();
        this.cpuLogicalCores = processor.getPhysicalProcessorCount();
        this.cpuModel = processor.getProcessorIdentifier().getName();
        this.sysRate = cpuInfo.getSys();
        this.userRate = cpuInfo.getUser();
        this.freeRate = cpuInfo.getFree();
        this.ticks = cpuInfo.getTicks();
    }
}
