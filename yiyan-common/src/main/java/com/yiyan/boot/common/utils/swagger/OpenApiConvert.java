package com.yiyan.boot.common.utils.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.tags.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author MENGJIAO
 * @createDate 2023-10-09 0009 上午 02:31
 */
public class OpenApiConvert {
    /**
     * 将OpenAPI对象转换为ApiInfo对象
     *
     * @param openAPI OpenAPI对象
     * @return ApiInfo对象集合
     */
    public static List<ApiInfo> convert(OpenAPI openAPI) {
        // 获取所有接口信息
        Paths paths = openAPI.getPaths();
        Set<String> apiPaths = paths.keySet();
        List<ApiInfo.Info> apiInfoList = new ArrayList<>(apiPaths.size());
        // 遍历接口信息，封装接口信息
        for (String apiPath : apiPaths) {
            // 获取接口信息
            Operation operation = paths.get(apiPath).readOperations().get(0);
            // 封装接口信息
            ApiInfo.Info apiInfo = new ApiInfo.Info(operation.getTags().get(0), apiPath, operation.getOperationId(), operation.getSummary(), operation.getDescription());
            apiInfoList.add(apiInfo);
        }
        // 获取所有标签信息
        List<Tag> tags = openAPI.getTags();
        List<ApiInfo> apiInfos = new ArrayList<>(tags.size());
        // 根据标签信息给API分组
        for (Tag tag : tags) {
            List<ApiInfo.Info> tagInfos = apiInfoList.stream().filter(apiInfo -> apiInfo.getTag().equals(tag.getName())).collect(Collectors.toList());
            ApiInfo apiInfo = new ApiInfo(tag.getName(), tag.getDescription(), tagInfos);
            apiInfos.add(apiInfo);
        }
        return apiInfos;
    }
}
