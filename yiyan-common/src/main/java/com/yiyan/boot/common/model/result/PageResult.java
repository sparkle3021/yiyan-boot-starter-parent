package com.yiyan.boot.common.model.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yiyan.boot.common.utils.ObjectUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页数据返回实体
 *
 * @author MENGJIAO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PageResult<T> extends BaseResult {


    private PageResultRecord<T> data;

    @Data
    public static class PageResultRecord<T> {
        private Long current;

        private Long pageSize;

        private Long total;

        private List<T> records;
    }

    public static <T> PageResult<T> success(IPage<T> result) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setCode(CODE_SUCCESS);
        pageResult.setMessage(QUERY_SUCCESS);
        PageResultRecord<T> data = new PageResultRecord<>();
        data.setCurrent(result.getCurrent());
        data.setPageSize(result.getSize());
        data.setTotal(ObjectUtils.isNotEmpty(result.getTotal()) ? result.getTotal() : 0L);
        data.setRecords(result.getRecords());
        pageResult.setData(data);
        return pageResult;
    }

    public static <T> PageResult<T> success(Long total, List<T> records) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setCode(CODE_SUCCESS);
        pageResult.setMessage(QUERY_SUCCESS);
        PageResultRecord<T> data = new PageResultRecord<>();
        data.setTotal(total);
        data.setRecords(records);
        pageResult.setData(data);
        return pageResult;
    }

}

