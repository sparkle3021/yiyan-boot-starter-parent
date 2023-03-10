package com.oho.common.utils.mail;

import com.oho.common.enums.YesNoEnum;
import com.oho.common.exception.Asserts;
import com.oho.common.utils.CollectionUtils;
import com.oho.common.utils.StringUtils;
import com.oho.common.utils.mail.model.MailContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;

/**
 * 邮件工具类
 *
 * @author MENGJIAO
 */
@Component
public class MailUtils {

    private final Logger log = LoggerFactory.getLogger(MailUtils.class);

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 启用Html
     */
    private static final boolean ENABLE_HTML = YesNoEnum.YES.getValue();
    /**
     * 启用附件
     */
    private static final boolean ENABLE_MULTIPART = YesNoEnum.YES.getValue();

    /**
     * 校验邮件必须内容
     */
    private void checkMailContent(MailContent mailContent) {
        if (StringUtils.isBlank(mailContent.getFrom())) {
            log.error("邮件错误：发件人信息不能为空");
            Asserts.fail("邮件错误：发件人信息不能为空");
        }

        if (CollectionUtils.isEmpty(mailContent.getTo())) {
            log.error("邮件错误：收件人不能为空");
            Asserts.fail("邮件错误：收件人不能为空");
        }

        if (StringUtils.isBlank(mailContent.getSubject())) {
            log.error("邮件错误：邮件标题不能为空");
            Asserts.fail("邮件错误：邮件标题不能为空");
        }

        if (StringUtils.isBlank(mailContent.getContent())) {
            log.error("邮件错误：邮件内容不能为空");
            Asserts.fail("邮件错误：邮件内容不能为空");
        }
    }

    /**
     * 发送邮件
     */
    public Boolean sendEmail(MailContent mailContent) {
        checkMailContent(mailContent);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            // 创建multipart message
            MimeMessageHelper multipartMessage = new MimeMessageHelper(message, ENABLE_MULTIPART);
            // 设置发件人
            multipartMessage.setFrom(mailContent.getFrom());
            // 设置收件人
            multipartMessage.setTo(CollectionUtils.listToArray(mailContent.getTo(), String.class));
            // 设置抄送人
            if (CollectionUtils.isNotEmpty(mailContent.getCc())) {
                multipartMessage.setCc(CollectionUtils.listToArray(mailContent.getCc(), String.class));
            }
            // 设置密送
            if (CollectionUtils.isNotEmpty(mailContent.getBcc())) {
                multipartMessage.setBcc(CollectionUtils.listToArray(mailContent.getBcc(), String.class));
            }
            // 设置邮件标题
            multipartMessage.setSubject(mailContent.getSubject());
            // 设置邮件内容
            multipartMessage.setText(mailContent.getContent(), ENABLE_HTML);
            // 添加附件
            for (MultipartFile attachmentFile : mailContent.getAttachmentFiles()) {
                multipartMessage.addAttachment(attachmentFile.getName(), attachmentFile);
            }
            mailSender.send(message);
        } catch (Exception e) {
            log.error("邮件发送失败 ： [{}] - [{}]", e.getMessage(), e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

}
