package com.yiyan.boot.payment.alipay.core.model;

import com.alipay.easysdk.payment.huabei.models.HuabeiConfig;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Sparkler
 * @createDate 2023/1/14
 */
@Data
public class AlipayHuabeiParam implements Serializable {

    /**
     * 订单标题
     */
    private String subject;

    /**
     * 交易创建时传入的商户订单号
     */
    private String outTradeNo;

    /**
     * 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
     */
    private String totalAmount;

    /**
     * 买家的支付宝用户ID，如果为空，会从传入的码值信息中获取买家ID
     */
    private String buyerId;

    /**
     * 花呗交易扩展参数:
     * hbFqNum : 花呗分期数，仅支持传入3、6、12
     * hbFqSellerPercent : 代表卖家承担收费比例，商家承担手续费传入100，用户承担手续费传入0，仅支持传入100、0两种
     */
    private HuabeiConfig extendParams;

}
