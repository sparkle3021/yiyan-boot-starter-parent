package com.yiyan.boot.mail.aliyun.core.model.task;

import lombok.Data;

/**
 * @author MENGJIAO
 * @createDate 2023-05-04 17:37
 */
@Data
public class DmTaskQueryDTO {

    private String keyWord;
    /**
     * 0表示正常
     * 1表示不正常
     */
    private Integer status;

    private Integer pageSize = 1;

    private Integer pageNo = 10;
}
