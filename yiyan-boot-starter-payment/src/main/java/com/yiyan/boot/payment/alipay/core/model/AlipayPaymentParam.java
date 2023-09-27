package com.yiyan.boot.payment.alipay.core.model;

import lombok.Data;

/**
 * The type Alipay pc payment param.
 *
 * @author Sparkler
 * @createDate 2023 /1/14
 */
@Data
public class AlipayPaymentParam {

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
     * 用户付款中途退出返回商户网站的地址
     */
    private String quitUrl;

    /**
     * 支付成功后同步跳转的页面，是一个http/https开头的字符串
     */
    private String returnUrl;

    /**
     * 支付授权码，即买家的付款码数字
     */
    private String authCode;

    /**
     * 买家的支付宝唯一用户号（2088开头的16位纯数字）
     */
    private String buyerId;

}
