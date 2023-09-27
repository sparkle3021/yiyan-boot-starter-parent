package com.yiyan.boot.common.utils.oshi.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import oshi.software.os.OperatingSystem;

/**
 * @author Sparkler
 * @createDate 2022/12/10
 */
@Data
@NoArgsConstructor
public class OsInfo {

    /**
     * 系统厂商
     */
    private String manufacturer;

    /**
     * 系统名
     */
    private String osName;

    /**
     * 版本号
     */
    private String version;

    /**
     * 编译编号
     */
    private String buildNumber;

    /**
     * 当前系统线程数
     */
    private int threadCount;

    public OsInfo(OperatingSystem os) {
        init(os);
    }

    private void init(OperatingSystem os) {
        this.manufacturer = os.getManufacturer();
        this.osName = os.getFamily();
        this.version = os.getVersionInfo().getVersion();
        this.buildNumber = os.getVersionInfo().getBuildNumber();
        this.threadCount = os.getThreadCount();
    }
}
