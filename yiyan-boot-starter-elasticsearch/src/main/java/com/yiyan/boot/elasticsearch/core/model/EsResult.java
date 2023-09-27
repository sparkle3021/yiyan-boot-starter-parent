package com.yiyan.boot.elasticsearch.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;

import java.util.List;

/**
 * @author Sparkler
 * @createDate 2023/1/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsResult<T> {
    /**
     * 查询结果
     */
    private List<SearchHit<T>> data;

    /**
     * 分页总页数
     */
    private Integer totalPages;

    /**
     * 分页总元素数
     */
    private Long totalElements;

    /**
     * 当前页
     */
    private Integer number;

    /**
     * 当前页大小
     */
    private Integer size;

    public EsResult(SearchHits<T> data) {
        this.data = data.getSearchHits();
    }

    public EsResult<T> convert(SearchPage<T> page) {
        EsResult<T> esResult = new EsResult<>();
        esResult.setData(page.getSearchHits().getSearchHits());
        esResult.setTotalPages(page.getTotalPages());
        esResult.setTotalElements(page.getTotalElements());
        esResult.setNumber(page.getNumber());
        esResult.setSize(page.getSize());
        return esResult;
    }

}
