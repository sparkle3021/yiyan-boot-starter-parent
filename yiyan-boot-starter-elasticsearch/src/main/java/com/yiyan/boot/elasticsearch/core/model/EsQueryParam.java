package com.yiyan.boot.elasticsearch.core.model;

import com.yiyan.boot.common.utils.BeanUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Elasticsearch 查询参数
 *
 * @author Sparkler
 * @createDate 2023/1/7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsQueryParam implements Serializable {

    /**
     * 设置默认返回数据大小，Elasticsearch 默认返回10条
     */
    private final PageRequest DEFAULT_PAGE = PageRequest.of(0, 20);

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
     * 排序参数
     */
    private Map<String, SortOrder> sort;

    /**
     * 查询包含的字段
     */
    private String[] includeFields;

    /**
     * 查询排除的字段
     */
    private String[] excludeFields;

    /**
     * 范围查询参数
     */
    private List<EsRangeParam> esRangeParam;

    /**
     * 高亮列
     */
    private List<String> highlightFields;

    /**
     * 高亮前缀标签
     */
    private String preTags;

    /**
     * 高亮后缀标签
     */
    private String postTags;

    /**
     * 分页查询参数
     */
    private PageRequest page = DEFAULT_PAGE;

    /**
     * 复合查询参数
     */
    private List<EsBoolQueryParam> boolQueryParam;

    public static EsQueryParam convert(EsBoolQueryParam boolQueryParam) {
        return BeanUtils.copyProperties(boolQueryParam, EsQueryParam.class);
    }
}
