package com.oho.common.utils.oshi.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.VirtualMemory;

/**
 * 内存信息
 *
 * @author Sparkler
 * @createDate 2022/12/10
 */
@Data
@NoArgsConstructor
public class MemoryInfo {

    /**
     * 内存容量 单位bit
     */
    private long memoryTotal;

    /**
     * 已使用的内存大小，单位bit
     */
    private long memoryUsed;

    /**
     * 可用内存大小，单位bit
     */
    private long memoryAvailable;

    /**
     * 内存类型 -- DDR4...
     */
    private String memoryType;

    /**
     * 内存频率，单位bit
     */
    private long memorySpeed;

    /**
     * 虚拟内存大小，单位bit
     */
    private long virtualMemoryTotal;

    /**
     * 使用中的虚拟内存大小，单位bit
     */
    private long virtualMemoryInUse;

    /**
     * swap 分区大小，单位bit
     */
    private long swapTotal;

    /**
     * 已使用的swap分区，单位bit
     */
    private long swapUsed;

    public MemoryInfo(GlobalMemory memory) {
        init(memory);
    }

    public void init(GlobalMemory memory) {
        this.memoryTotal = memory.getTotal();
        this.memoryAvailable = memory.getAvailable();
        this.memoryUsed = memoryTotal - memoryAvailable;
        this.memoryType = memory.getPhysicalMemory().get(0).getMemoryType();
        this.memorySpeed = memory.getPhysicalMemory().get(0).getClockSpeed();
        VirtualMemory virtualMemory = memory.getVirtualMemory();
        this.virtualMemoryTotal = virtualMemory.getVirtualMax();
        this.virtualMemoryInUse = virtualMemory.getVirtualInUse();
        this.swapTotal = virtualMemory.getSwapTotal();
        this.swapUsed = virtualMemory.getSwapUsed();
    }
}
