package com.oho.common.exception;


import com.oho.common.enums.ErrorCodeEnum;
import com.oho.common.enums.ErrorCodeEnumFormat;

/**
 * 断言处理类，用于抛出各种API异常
 *
 * @author macro
 * @date 2020 /2/27
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
}
