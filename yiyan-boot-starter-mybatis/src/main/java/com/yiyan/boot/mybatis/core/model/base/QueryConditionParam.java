package com.yiyan.boot.mybatis.core.model.base;

import lombok.Data;

/**
 * 查询条件参数
 *
 * @author MENGJIAO
 * @createDate 2023-10-10 0010 下午 10:29
 */
@Data
public class QueryConditionParam {
    /**
     * 查询字段
     */
    private String field;
    /**
     * 查询值
     */
    private String value;
}
