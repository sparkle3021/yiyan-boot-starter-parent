package com.oho.mybatis.core.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务分页模板
 *
 * @author Sparkler
 * @createDate 2023/2/15-12:13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BasePage<T> extends Page<T> {

}