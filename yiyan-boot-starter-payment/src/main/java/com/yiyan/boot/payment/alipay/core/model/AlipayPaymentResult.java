package com.yiyan.boot.payment.alipay.core.model;

import lombok.Data;

/**
 * @author Sparkler
 * @createDate 2023/1/14
 */
@Data
public class AlipayPaymentResult {

    /**
     * 支付宝订单号
     */
    private String tradeNo;

    /**
     * 业务订单号
     */
    private String outTradeNo;
}
