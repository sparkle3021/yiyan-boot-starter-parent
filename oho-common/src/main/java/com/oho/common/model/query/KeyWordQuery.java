package com.oho.common.model.query;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 关键字查询条件
 *
 * @author baiyan
 * @date 2020/11/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KeyWordQuery extends PageQuery {

    @ApiModelProperty("关键字查询")
    private String keyWord;

    @Override
    public IPage setPages(long pages) {
        return super.setPages(pages);
    }
}
