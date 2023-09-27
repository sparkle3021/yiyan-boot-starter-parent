package com.yiyan.boot.common.utils.oshi;

import cn.hutool.system.oshi.OshiUtil;
import com.yiyan.boot.common.utils.oshi.model.*;
import lombok.extern.slf4j.Slf4j;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import oshi.software.os.OSFileStore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统信息获取
 *
 * @author Sparkler
 * @createDate 2022 /12/10
 */
@Slf4j
public class SystemInfoUtils {

    /**
     * 获取系统当前参数
     *
     * @return system info
     */
    public static SystemInfo systemInfo() {
        SystemInfo systemInfo = new SystemInfo();
        systemInfo.setOsInfo(osInfo());
        systemInfo.setCpusInfo(cpuInfo());
        systemInfo.setMemoryInfo(memoryInfo());
        systemInfo.setDiskInfos(diskInfo());
        return systemInfo;
    }

    /**
     * 获取当前操作系统信息
     *
     * @return os info
     */
    public static OsInfo osInfo() {
        return new OsInfo(OshiUtil.getOs());
    }

    /**
     * 获取当前系统Cpu信息
     *
     * @return cpus info
     */
    public static CpusInfo cpuInfo() {
        return new CpusInfo(OshiUtil.getProcessor(), OshiUtil.getCpuInfo());
    }

    /**
     * 获取当前系统内存信息
     *
     * @return memory info
     */
    public static MemoryInfo memoryInfo() {
        return new MemoryInfo(OshiUtil.getMemory());
    }

    /**
     * 系统磁盘信息
     *
     * @return list
     */
    public static List<DiskInfo> diskInfo() {
        // 获取系统所有分区信息
        List<OSFileStore> fileStores = OshiUtil.getOs().getFileSystem().getFileStores();

        // 获取磁盘信息
        List<HWDiskStore> diskStores = OshiUtil.getDiskStores();
        List<DiskInfo> diskInfos = new ArrayList<>(diskStores.size());
        for (HWDiskStore hwDiskStore : diskStores) {
            DiskInfo diskInfo = new DiskInfo();
            diskInfo.setDiskName(hwDiskStore.getName());
            diskInfo.setModel(hwDiskStore.getModel());
            diskInfo.setSize(hwDiskStore.getSize());

            // 获取磁盘分区信息
            List<String> partitionUuids = hwDiskStore.getPartitions().stream().map(HWPartition::getUuid).collect(Collectors.toList());
            List<DiskInfo.PartitionInfo> partitions = new ArrayList<>(partitionUuids.size());
            for (String partitionUuid : partitionUuids) {
                OSFileStore osFileStore = fileStores.stream()
                        .filter(fileStore -> fileStore.getUUID().equals(partitionUuid))
                        .collect(Collectors.toList()).get(0);
                DiskInfo.PartitionInfo partitionInfo = new DiskInfo.PartitionInfo();
                partitionInfo.setPartitionName(osFileStore.getName());
                partitionInfo.setPartitionTable(osFileStore.getLabel());
                partitionInfo.setMount(osFileStore.getMount());
                partitionInfo.setFsType(osFileStore.getType());
                partitionInfo.setDescription(osFileStore.getDescription());
                partitionInfo.setTotalSpace(osFileStore.getTotalSpace());
                partitionInfo.setFreeSpace(osFileStore.getFreeSpace());
                partitionInfo.setUsableSpace(osFileStore.getUsableSpace());
                partitionInfo.setUsedSpace(partitionInfo.getTotalSpace() - partitionInfo.getFreeSpace());
                partitions.add(partitionInfo);
            }
            diskInfo.setPartitions(partitions);
            diskInfos.add(diskInfo);
        }
        return diskInfos;
    }
}
