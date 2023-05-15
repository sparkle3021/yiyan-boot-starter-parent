package com.oho.mail.aliyun.core.model.content;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 阿里云DM单邮件发送参数
 *
 * @author MENGJIAO
 * @createDate 2023-05-04 15:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DmSingleSendMail extends DmBaseEmailProperties {
    /**
     * 目标地址
     */
    private String toAddress;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件HTML正文
     */
    private String htmlBody;

    /**
     * 邮件纯文本正文
     */
    private String textBody;

    /**
     * 发信人昵称
     */
    private String fromAlias;

    /**
     * 回信地址
     */
    private String replyAddress;

    /**
     * 回信地址昵称
     */
    private String replyAddressAlias;

    private Boolean replyToAddress;

}
