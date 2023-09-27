package com.yiyan.boot.common.exception;


import com.yiyan.boot.common.enums.ErrorCodeEnum;
import com.yiyan.boot.common.enums.ErrorCodeEnumFormat;

/**
 * 断言处理类，用于抛出各种API异常
 *
 * @author MENGJIAO
 * @createDate 2020-2-27
 */
public class Asserts {
    /**
     * Fail.
     *
     * @param message the message
     */
    public static void fail(String message) {
        throw new BizException(message);
    }

    /**
     * Fail.
     *
     * @param errorCode the error code
     */
    public static void fail(ErrorCodeEnumFormat errorCode) {
        throw new BizException(errorCode);
    }

    /**
     * Fail.
     */
    public static void fail(ErrorCodeEnumFormat errorCode, String message) {
        throw new BizException(errorCode, message);
    }

    /**
     * Fail.
     */
    public static void fail() {
        throw new BizException(ErrorCodeEnum.BIZ_ERROR);
    }

    /**
     * Fail.
     */
    public static void fail(boolean flag, String errorMessage) {
        if (flag) {
            throw new BizException(errorMessage);
        }
    }
}
