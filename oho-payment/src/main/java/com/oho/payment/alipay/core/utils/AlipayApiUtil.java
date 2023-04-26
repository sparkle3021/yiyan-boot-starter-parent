package com.oho.payment.alipay.core.utils;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.app.models.AlipayTradeAppPayResponse;
import com.alipay.easysdk.payment.common.models.*;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePayResponse;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
import com.alipay.easysdk.payment.huabei.models.AlipayTradeCreateResponse;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.alipay.easysdk.payment.wap.models.AlipayTradeWapPayResponse;
import com.oho.payment.alipay.core.model.AlipayHuabeiParam;
import com.oho.payment.alipay.core.model.AlipayPaymentParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Alipay API 工具类
 *
 * @author Sparkler
 * @createDate 2023 /1/14
 */
@Slf4j
@Component
public class AlipayApiUtil {

    /**
     * 创建花呗分期交易
     *
     * @param paymentParam 创建花呗支付参数
     * @return the alipay payment result
     */
    public AlipayTradeCreateResponse huabei(@NotNull AlipayHuabeiParam paymentParam) {
        try {
            AlipayTradeCreateResponse alipayHuabeiResponse =
                    Factory.Payment
                            .Huabei().create(paymentParam.getSubject(), paymentParam.getOutTradeNo(), paymentParam.getTotalAmount(), paymentParam.getBuyerId(), paymentParam.getExtendParams());
            if (ResponseChecker.success(alipayHuabeiResponse)) {
                return alipayHuabeiResponse;
            } else {
                log.error("Alipay [花呗支付] 创建失败。 err_code : {} ,err_msg : {}", alipayHuabeiResponse.getSubCode(), alipayHuabeiResponse.getSubMsg());
                return null;
            }
        } catch (Exception e) {
            System.err.println("Alipay [花呗支付API] 调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 当面付交易付款
     *
     * @param paymentParam 支付宝支付订单创建参数
     * @return the string
     */
    public AlipayTradePayResponse tradePay(@NotNull AlipayPaymentParam paymentParam) {
        try {
            AlipayTradePayResponse alipayTradePayResponse =
                    Factory.Payment
                            .FaceToFace().pay(paymentParam.getSubject(), paymentParam.getOutTradeNo(), paymentParam.getTotalAmount(), paymentParam.getAuthCode());
            if (ResponseChecker.success(alipayTradePayResponse)) {
                return alipayTradePayResponse;
            }
            log.error("Alipay [当面付交易付款] 创建失败。 err_code : {} ,err_msg : {}", alipayTradePayResponse.getSubCode(), alipayTradePayResponse.getSubMsg());
            return null;
        } catch (Exception e) {
            System.err.println("Alipay [当面付交易付款API] 调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 交易预创建，生成正扫二维码
     *
     * @param paymentParam 支付宝支付订单创建参数
     * @return the string
     */
    public AlipayTradePrecreateResponse tradePreCreate(@NotNull AlipayPaymentParam paymentParam) {
        try {
            AlipayTradePrecreateResponse alipayTradePrecreateResponse =
                    Factory.Payment
                            .FaceToFace().preCreate(paymentParam.getSubject(), paymentParam.getOutTradeNo(), paymentParam.getTotalAmount());
            if (ResponseChecker.success(alipayTradePrecreateResponse)) {
                return alipayTradePrecreateResponse;
            }
            log.error("Alipay [交易预创建，生成正扫二维码] 创建失败。 err_code : {} ,err_msg : {}", alipayTradePrecreateResponse.getSubCode(), alipayTradePrecreateResponse.getSubMsg());
            return null;
        } catch (Exception e) {
            System.err.println("Alipay [交易预创建，生成正扫二维码API] 调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 电脑网页支付
     *
     * @param paymentParam 支付宝支付订单创建参数
     * @return 电脑网页支付创建结果
     */
    public AlipayTradePagePayResponse pagePay(AlipayPaymentParam paymentParam) {
        try {
            AlipayTradePagePayResponse alipayTradePagePayResponse =
                    Factory.Payment
                            .Page().pay(paymentParam.getSubject(), paymentParam.getOutTradeNo(), paymentParam.getTotalAmount(), paymentParam.getReturnUrl());
            if (ResponseChecker.success(alipayTradePagePayResponse)) {
                return alipayTradePagePayResponse;
            }
            log.error("Alipay [电脑网页支付] 创建失败。 result_body: {}", alipayTradePagePayResponse.getBody());
            return null;
        } catch (Exception e) {
            System.err.println("Alipay [电脑网页支付API] 调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 手机网站支付
     *
     * @param paymentParam 支付宝支付订单创建参数
     * @return the alipay trade wap pay response
     */
    public AlipayTradeWapPayResponse wapPay(AlipayPaymentParam paymentParam) {
        try {
            AlipayTradeWapPayResponse alipayTradeWapPayResponse =
                    Factory.Payment
                            .Wap().pay(paymentParam.getSubject(), paymentParam.getOutTradeNo(), paymentParam.getTotalAmount(), paymentParam.getQuitUrl(), paymentParam.getReturnUrl());
            if (ResponseChecker.success(alipayTradeWapPayResponse)) {
                return alipayTradeWapPayResponse;
            }
            log.error("Alipay [手机网站支付] 创建失败。 result_body: {}", alipayTradeWapPayResponse.getBody());
            return null;
        } catch (Exception e) {
            System.err.println("Alipay [手机网站支付创建API] 调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 手机APP支付
     *
     * @param paymentParam 支付宝支付订单创建参数
     * @return 手机APP支付创建结果
     */
    public AlipayTradeAppPayResponse appPay(AlipayPaymentParam paymentParam) {
        try {
            AlipayTradeAppPayResponse alipayTradeAppPayResponse =
                    Factory.Payment
                            .App().pay(paymentParam.getSubject(), paymentParam.getOutTradeNo(), paymentParam.getTotalAmount());
            if (ResponseChecker.success(alipayTradeAppPayResponse)) {
                return alipayTradeAppPayResponse;
            }
            log.error("Alipay [手机APP支付] 创建失败。 result_body: {}", alipayTradeAppPayResponse.getBody());
            return null;
        } catch (Exception e) {
            System.err.println("Alipay [手机APP支付创建API] 调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 创建订单
     *
     * @param paymentParam 支付宝支付订单创建参数
     * @return 创建订单结果 com . alipay . easysdk . payment . common . models . alipay trade create response
     */
    public com.alipay.easysdk.payment.common.models.AlipayTradeCreateResponse tradeCreate(AlipayPaymentParam paymentParam) {
        try {
            com.alipay.easysdk.payment.common.models.AlipayTradeCreateResponse alipayTradeCreateResponse =
                    Factory.Payment.Common()
                            .create(paymentParam.getSubject(), paymentParam.getOutTradeNo(), paymentParam.getTotalAmount(), paymentParam.getBuyerId());
            if (ResponseChecker.success(alipayTradeCreateResponse)) {
                return alipayTradeCreateResponse;
            }
            log.error("Alipay [通用接口创建订单API] 失败。 err_code : {} ,err_msg : {}", alipayTradeCreateResponse.getSubCode(), alipayTradeCreateResponse.getSubMsg());
            return null;
        } catch (Exception e) {
            System.err.println("Alipay [通用接口创建订单API] 调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 查询交易
     *
     * @param outTradeNo 交易创建时传入的商户订单号
     * @return 查询交易结果 alipay trade query response
     */
    public AlipayTradeQueryResponse tradeQuery(@NotBlank String outTradeNo) {
        try {
            AlipayTradeQueryResponse alipayTradeQueryResponse = Factory.Payment.Common().query(outTradeNo);
            if (ResponseChecker.success(alipayTradeQueryResponse)) {
                return alipayTradeQueryResponse;
            }
            log.error("Alipay查询交易失败。 err_code : {} ,err_msg : {}", alipayTradeQueryResponse.getSubCode(), alipayTradeQueryResponse.getSubMsg());
            return null;
        } catch (Exception e) {
            System.err.println("Alipay查询交易API 调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 交易退款
     *
     * @param outTradeNo   交易创建时传入的商户订单号
     * @param refundAmount 需要退款的金额，该金额不能大于订单金额，单位为元，支持两位小数
     * @return 交易退款结果 alipay trade refund response
     */
    public AlipayTradeRefundResponse tradeRefund(@NotBlank String outTradeNo, @NotBlank String refundAmount) {
        try {
            AlipayTradeRefundResponse alipayTradeRefundResponse =
                    Factory.Payment.Common().refund(outTradeNo, refundAmount);
            if (ResponseChecker.success(alipayTradeRefundResponse)) {
                return alipayTradeRefundResponse;
            }
            log.error("Alipay交易退款失败。 err_code : {} ,err_msg : {}", alipayTradeRefundResponse.getSubCode(), alipayTradeRefundResponse.getSubMsg());
            return null;
        } catch (Exception e) {
            System.err.println("Alipay交易退款API 调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 交易关闭
     *
     * @param outTradeNo 交易创建时传入的商户订单号
     * @return 交易关闭结果 alipay trade close response
     */
    public AlipayTradeCloseResponse tradeClose(@NotBlank String outTradeNo) {
        try {
            AlipayTradeCloseResponse alipayTradeCloseResponse =
                    Factory.Payment.Common().close(outTradeNo);
            if (ResponseChecker.success(alipayTradeCloseResponse)) {
                return alipayTradeCloseResponse;
            }
            log.error("Alipay交易关闭失败。 err_code : {} ,err_msg : {}", alipayTradeCloseResponse.getSubCode(), alipayTradeCloseResponse.getSubMsg());
            return null;
        } catch (Exception e) {
            System.err.println("Alipay交易关闭API 调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 交易取消
     *
     * @param outTradeNo 交易创建时传入的商户订单号
     * @return 交易取消结果 alipay trade cancel response
     */
    public AlipayTradeCancelResponse tradeCancel(@NotBlank String outTradeNo) {
        try {
            AlipayTradeCancelResponse alipayTradeCancelResponse =
                    Factory.Payment.Common().cancel(outTradeNo);
            if (ResponseChecker.success(alipayTradeCancelResponse)) {
                return alipayTradeCancelResponse;
            }
            log.error("Alipay交易取消失败。 err_code : {} ,err_msg : {}", alipayTradeCancelResponse.getSubCode(), alipayTradeCancelResponse.getSubMsg());
            return null;
        } catch (Exception e) {
            System.err.println("Alipay交易取消API 调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 交易退款查询
     *
     * @param outTradeNo   交易创建时传入的商户订单号
     * @param outRequestNo 请求退款接口时，传入的退款请求号，如果在退款请求时未传入，则该值为创建交易时的外部交易号
     * @return 交易退款查询结果 alipay trade fastpay refund query response
     */
    public AlipayTradeFastpayRefundQueryResponse queryRefund(@NotBlank String outTradeNo, @NotBlank String outRequestNo) {
        try {
            AlipayTradeFastpayRefundQueryResponse alipayTradeFastpayRefundQueryResponse =
                    Factory.Payment.Common().queryRefund(outTradeNo, outRequestNo);
            if (ResponseChecker.success(alipayTradeFastpayRefundQueryResponse)) {
                return alipayTradeFastpayRefundQueryResponse;
            }
            log.error("Alipay [交易退款查询] 失败。 err_code : {} ,err_msg : {}", alipayTradeFastpayRefundQueryResponse.getSubCode(), alipayTradeFastpayRefundQueryResponse.getSubMsg());
            return null;
        } catch (Exception e) {
            System.err.println("Alipay [交易退款查询API] 调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 查询对账单下载地址
     *
     * @param billType 账单类型，商户通过接口或商户经开放平台授权后其所属服务商通过接口可以获取以下账单类型：trade、signcustomer；                 trade指商户基于支付宝交易收单的业务账单；                 signcustomer是指基于商户支付宝余额收入及支出等资金变动的帐务账单
     * @param billDate 账单时间：日账单格式为yyyy-MM-dd，最早可下载2016年1月1日开始的日账单；月账单格式为yyyy-MM，最早可下载2016年1月开始的月账单
     * @return 账单下载地址 alipay data dataservice bill downloadurl query response
     */
    public AlipayDataDataserviceBillDownloadurlQueryResponse downloadBill(@NotBlank String billType, @NotBlank String billDate) {
        try {
            AlipayDataDataserviceBillDownloadurlQueryResponse alipayDataDataserviceBillDownloadurlQueryResponse =
                    Factory.Payment.Common().downloadBill(billType, billDate);
            if (ResponseChecker.success(alipayDataDataserviceBillDownloadurlQueryResponse)) {
                return alipayDataDataserviceBillDownloadurlQueryResponse;
            }
            log.error("Alipay [交易退款查询] 失败。 err_code : {} ,err_msg : {}", alipayDataDataserviceBillDownloadurlQueryResponse.getSubCode(), alipayDataDataserviceBillDownloadurlQueryResponse.getSubMsg());
            return null;
        } catch (Exception e) {
            System.err.println("Alipay [交易退款查询API] 调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 异步通知验签
     *
     * @param parameters 异步通知中收到的待验签的所有参数
     * @return TRUE /FALSE
     */
    public Boolean verifyNotify(@NotNull Map<String, String> parameters) {
        Boolean verifyNotifyResult;
        try {
            verifyNotifyResult = Factory.Payment.Common().verifyNotify(parameters);
        } catch (Exception e) {
            System.err.println("Alipay [异步通知验签] 调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
        return verifyNotifyResult;
    }
}
