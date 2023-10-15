package com.yiyan.boot.common.model.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiyan.boot.common.enums.QueryOrderEnum;
import com.yiyan.boot.common.utils.StringUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询参数
 *
 * @author MENGJIAO
 */
@Data
public class BasePageQuery<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 最大页面大小
     */
    private static final long MAX_PAGE_SIZE = 50L;
    /**
     * 升序
     */
    private static final String SORT_ASC = QueryOrderEnum.ASC.getValue();
    /**
     * 降序
     */
    private static final String SORT_DESC = QueryOrderEnum.DESC.getValue();

    /**
     * 当前页
     */
    private Long current;

    /**
     * 页面大小
     */
    private Long pageSize;

    /**
     * 排序字段。默认排序字段 id
     */
    private String sortField;

    /**
     * 排序规则。默认升序
     */
    private String sortOrder = SORT_ASC;

    /**
     * 是否统计total
     */
    private boolean searchCount = true;

    /**
     * 分页对象
     */
    private Page<T> page;

    /**
     * 开启驼峰转下划线，默认开启
     */
    private Boolean enableCamelToUnderline = true;

    /**
     * 驼峰转下划线
     *
     * @param sortField 排序字段
     */
    public void setSortField(String sortField) {
        String sortFieldTmp = sortField;
        if (this.enableCamelToUnderline) {
            sortFieldTmp = StringUtils.convertCamelToUnderline(sortField);
        }
        this.sortField = sortFieldTmp;
    }

    /**
     * 限制最大页面大小
     *
     * @param pageSize 页面大小
     */
    public void setPageSize(Long pageSize) {
        if (pageSize > MAX_PAGE_SIZE) {
            pageSize = MAX_PAGE_SIZE;
        }
        this.pageSize = pageSize;
    }

    /**
     * 根据参数返回Page对象
     */
    public Page<T> getPage() {
        Page<T> tmpPage = new Page<>(current, pageSize);
        // 是否统计total
        tmpPage.setSearchCount(searchCount);
        return tmpPage;
    }

}
