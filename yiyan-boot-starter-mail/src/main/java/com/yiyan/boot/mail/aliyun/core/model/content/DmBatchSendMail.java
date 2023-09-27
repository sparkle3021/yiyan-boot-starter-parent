package com.yiyan.boot.mail.aliyun.core.model.content;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 阿里云DM批量邮件发送参数
 *
 * @author MENGJIAO
 * @createDate 2023-05-04 16:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DmBatchSendMail extends DmBaseEmailProperties {
    /**
     * 预先创建且通过审核的模板名称
     */
    public String templateName;

    /**
     * 预先创建且上传了收件人的收件人列表名称
     */
    public String receiversName;

}
