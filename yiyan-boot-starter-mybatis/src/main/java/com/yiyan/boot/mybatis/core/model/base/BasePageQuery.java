package com.yiyan.boot.mybatis.core.model.base;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import com.oho.common.enums.YesNoEnum;
import com.oho.common.utils.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author MENGJIAO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BasePageQuery<T> extends BasePage<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final long MAX_PAGE_SIZE = 30;

    private static final String SORT_ASC = "asc";

    private static final String SORT_DESC = "desc";

    /**
     * 页面大小
     */
    private Long pageSize;

    /**
     * 排序字段。默认排序字段 id
     */
    private String sortField = "id";

    /**
     * 排序规则。默认升序
     */
    private String sortOrder = "asc";

    @Override
    public void setMaxLimit(Long maxLimit) {
        // 单页分页条数限制
        super.setMaxLimit(MAX_PAGE_SIZE);
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
        super.setSize(pageSize);
    }

    @Override
    public List<OrderItem> orders() {
        boolean check = SqlInjectionUtils.check(this.sortField);
        OrderItem orderItem = new OrderItem();
        if (!check) {
            String field = StringUtils.camelToUnderline(this.sortField, YesNoEnum.NO.getValue());
            orderItem = sortOrder.equals(SORT_ASC) ? OrderItem.asc(field) : OrderItem.desc(field);
        }
        super.orders().add(orderItem);
        return super.orders();
    }

}
