package com.oho.common.utils.oshi.model;

import lombok.Data;

import java.util.List;

/**
 * 磁盘信息
 *
 * @author Sparkler
 * @createDate 2022/12/10
 */
@Data
public class DiskInfo {
    /**
     * 磁盘名
     */
    private String diskName;

    /**
     * 磁盘类型
     */
    private String model;

    /**
     * 磁盘大小，单位bit
     */
    private long size;

    /**
     * 磁盘分区信息
     */
    private List<PartitionInfo> partitions;

    @Data
    public static class PartitionInfo {
        /**
         * 分区名
         */
        private String partitionName;

        /**
         * 分区标题
         */
        private String partitionTable;

        /**
         * 分区挂载点
         */
        private String mount;

        /**
         * 分区文件管理系统类型
         */
        private String fsType;

        /**
         * 备注
         */
        private String description;

        /**
         * 分区总大小，单位bit
         */
        private long totalSpace;

        /**
         * 分区可用空间大小，单位bit
         */
        private long freeSpace;

        /**
         * 分区可使用空间，单位bit
         */
        private long usableSpace;

        /**
         * 已使用空间，单位bit
         */
        private long usedSpace;

    }
}
