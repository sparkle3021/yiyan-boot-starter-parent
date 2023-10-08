package com.yiyan.boot.common.utils.swagger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author MENGJIAO
 * @createDate 2023-10-09 0009 上午 02:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiInfo {

    /**
     * 接口标签
     */
    private String tag;

    /**
     * 标签描述
     */
    private String description;

    /**
     * 接口信息
     */
    private List<Info> apiInfoList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Info {
        /**
         * 接口标签
         */
        private String tag;
        /**
         * 接口路径
         */
        private String apiPath;
        /**
         * 接口名称
         */
        private String apiName;
        /**
         * 接口总结
         */
        private String summary;

        /**
         * 接口描述
         */
        private String description;
    }
}
