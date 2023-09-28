package com.yiyan.boot.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * 根据IP查询归属地工具类
 * 只有中国的数据绝大部分精确到了城市，其他国家部分数据只能定位到国家，后前的选项全部是0。
 *
 * @author Sparkler
 * @createDate 2023-2-23 14:26
 */
@Slf4j
public class Ip2RegionUtils {

    /**
     * IP查询文件，需要放在resources目录下
     */
    private static final String IP2REGION_XDB = "ip2region.xdb";

    /**
     * IP查询文件缓存
     */
    private static byte[] IP2REGION_XDB_CACHE = null;

    static {
        try {
            IP2REGION_XDB_CACHE = Searcher.loadContentFromFile(Objects.requireNonNull(Ip2RegionUtils.class.getResource("/" + IP2REGION_XDB)).toURI().getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据IP查询归属地
     * 返回格式：国家|区域|省份|城市|ISP
     *
     * @param ip the ip
     * @return region by ip
     */
    public static String getRegionByIp(@NotBlank String ip) {
        try {
            Searcher searcher;
            if (IP2REGION_XDB_CACHE == null) {
                IP2REGION_XDB_CACHE = Searcher.loadContentFromFile(Objects.requireNonNull(Ip2RegionUtils.class.getResource("/" + IP2REGION_XDB)).toURI().getPath());
            }
            searcher = Searcher.newWithBuffer(IP2REGION_XDB_CACHE);
            return searcher.search(ip);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "未知";
    }

    /**
     * 根据IP获取国家
     *
     * @param ip the ip
     * @return the country by ip
     */
    public static String getCountryByIp(String ip) {
        String region = getRegionByIp(ip);
        return region.split("\\|")[0];
    }

    /**
     * 根据IP获取区域
     *
     * @param ip the ip
     * @return the area by ip
     */
    public static String getAreaByIp(String ip) {
        String region = getRegionByIp(ip);
        return region.split("\\|")[1];
    }

    /**
     * 根据IP获取省份
     *
     * @param ip the ip
     * @return the province by ip
     */
    public static String getProvinceByIp(String ip) {
        String region = getRegionByIp(ip);
        return region.split("\\|")[2];
    }

    /**
     * 根据IP获取城市
     *
     * @param ip the ip
     * @return the city by ip
     */
    public static String getCityByIp(String ip) {
        String region = getRegionByIp(ip);
        return region.split("\\|")[3];
    }

    /**
     * 根据IP获取运营商
     *
     * @param ip the ip
     * @return the isp by ip
     */
    public static String getIspByIp(String ip) {
        String region = getRegionByIp(ip);
        return region.split("\\|")[4];
    }

}