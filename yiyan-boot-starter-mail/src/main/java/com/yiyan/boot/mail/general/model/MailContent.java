package com.yiyan.boot.mail.general.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 邮件消息体
 *
 * @author MENGJIAO
 */
@Data
public class MailContent {
    /**
     * 邮件发送人
     */
    private String from;

    /**
     * 邮件接收人
     */
    private List<String> to;

    /**
     * 邮件抄送人
     */
    private List<String> cc;

    /**
     * 邮件密送人
     */
    private List<String> bcc;

    /**
     * 邮件标题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 附件
     */
    private List<MultipartFile> attachmentFiles;

}
