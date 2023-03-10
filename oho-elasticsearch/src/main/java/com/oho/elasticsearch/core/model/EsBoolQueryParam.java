package com.oho.elasticsearch.core.model;


import com.oho.elasticsearch.core.model.enums.EsBoolEnum;
import com.oho.elasticsearch.core.model.enums.EsQueryBuilderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.index.query.Operator;

import java.util.List;

/**
 * Elasticsearch 复合查询
 *
 * @author Sparkler
 * @createDate 2023/1/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsBoolQueryParam {

    /**
     * 查询构建器类型
     */
    private EsQueryBuilderEnum queryBuilder;

    /**
     * 条件类型
     */
    private EsBoolEnum boolOption;

    /**
     * 单字段查询
     */
    private String field;

    /**
     * 多字段查询
     */
    private List<String> fields;

    /**
     * 匹配内容
     */
    private Object content;

    /**
     * 多个匹配内容
     */
    private List<Object> contents;

    /**
     * 查询条件连接符
     */
    private Operator operator;

    /**
     * 范围查询参数
     */
    private List<EsRangeParam> esRangeParam;

    public EsBoolQueryParam(EsQueryBuilderEnum queryBuilder, EsBoolEnum boolOption, String field, Operator operator, List<EsRangeParam> esRangeParam) {
        this.queryBuilder = queryBuilder;
        this.boolOption = boolOption;
        this.field = field;
        this.operator = operator;
        this.esRangeParam = esRangeParam;
    }

    public EsBoolQueryParam(EsQueryBuilderEnum queryBuilder, EsBoolEnum boolOption, String field, Object content) {
        this.queryBuilder = queryBuilder;
        this.boolOption = boolOption;
        this.field = field;
        this.content = content;
    }

    public EsBoolQueryParam(EsQueryBuilderEnum queryBuilder, EsBoolEnum boolOption, String field, Object content, List<Object> contents) {
        this.queryBuilder = queryBuilder;
        this.boolOption = boolOption;
        this.field = field;
        this.content = content;
        this.contents = contents;
    }

    public EsBoolQueryParam(EsQueryBuilderEnum queryBuilder, EsBoolEnum boolOption, String field, Object content, List<Object> contents, Operator operator) {
        this.queryBuilder = queryBuilder;
        this.boolOption = boolOption;
        this.field = field;
        this.content = content;
        this.contents = contents;
        this.operator = operator;
    }

    public EsBoolQueryParam(EsQueryBuilderEnum queryBuilder, EsBoolEnum boolOption, List<String> fields, Object content, Operator operator) {
        this.queryBuilder = queryBuilder;
        this.boolOption = boolOption;
        this.fields = fields;
        this.content = content;
        this.operator = operator;
    }
}
