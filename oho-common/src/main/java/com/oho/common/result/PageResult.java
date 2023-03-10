package com.oho.common.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页数据返回实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PageResult<T> extends BaseResult {

    private Long current;

    private Long pageSize;

    private Long total;

    private List<T> records;

    public static <T> PageResult<T> success(IPage<T> result) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setCode(CODE_SUCCESS);
        pageResult.setMessage(QUERY_SUCCESS);
        pageResult.setCurrent(result.getCurrent());
        pageResult.setPageSize(result.getSize());
        pageResult.setTotal(result.getTotal());
        pageResult.setRecords(result.getRecords());
        return pageResult;
    }

    public static <T> PageResult<T> success(Long total, List<T> records) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setCode(CODE_SUCCESS);
        pageResult.setMessage(QUERY_SUCCESS);
        pageResult.setTotal(total);
        pageResult.setRecords(records);
        return pageResult;
    }

}

