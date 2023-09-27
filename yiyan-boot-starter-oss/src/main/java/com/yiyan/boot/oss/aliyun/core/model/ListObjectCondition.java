package com.yiyan.boot.oss.aliyun.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author MENGJIAO
 * @createDate 2023-04-27 12:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListObjectCondition {
    String bucketName;
    /**
     * prefix	限定返回的文件必须以prefix作为前缀。
     */
    String prefix;
    /**
     * delimiter	对文件名称进行分组的一个字符。所有名称包含指定的前缀且第一次出现delimiter字符之间的文件作为一组元素（commonPrefixes）。
     */
    String delimiter;
    /**
     * marker	列举指定marker之后的文件。指定该参数后，以marker为起点按文件名称的字母排序返回文件。
     */
    String marker;
    /**
     * maxKeys	限定此次列举文件的最大个数。指定该参数后，按文件名称的字母排序返回结果。默认值为100，最大值为1000。
     */
    Integer maxKeys;
}
