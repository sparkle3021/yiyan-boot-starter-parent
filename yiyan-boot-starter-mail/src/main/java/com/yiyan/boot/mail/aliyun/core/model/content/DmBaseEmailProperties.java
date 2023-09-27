package com.yiyan.boot.mail.aliyun.core.model.content;

import com.yiyan.boot.mail.aliyun.core.enums.AddressTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 阿里云DM基础邮件参数
 *
 * @author MENGJIAO
 * @createDate 2023-05-04 16:39
 */
@Data
public class DmBaseEmailProperties {
    /**
     * 管理控制台中配置的发信地址
     */
    @NotBlank(message = "发信地址不能为空")
    public String accountName;

    /**
     * 地址类型 0：为随机账号 1：为发信地址
     */
    @NotBlank(message = "地址类型不能为空")
    public AddressTypeEnum addressType;

    /**
     * 标签
     */
    public String tagName;

    /**
     * 回信地址
     */
    public String replyAddress;

    /**
     * 回信地址昵称
     */
    public String replyAddressAlias;

    /**
     * 为打开数据跟踪功能
     * </p>
     * 1：为打开数据跟踪功能
     * 0（默认）：为关闭数据跟踪功能。
     */
    public String clickTrace;

    public Long ownerId;

    public String resourceOwnerAccount;

    public Long resourceOwnerId;
}
