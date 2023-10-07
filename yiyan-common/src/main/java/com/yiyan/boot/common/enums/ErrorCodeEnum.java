package com.yiyan.boot.common.enums;

import lombok.Getter;

/**
 * The enum Result code enum.
 *
 * @author Sparkler
 * @createDate 2022 /11/9
 */
@Getter
public enum ErrorCodeEnum implements ErrorCodeEnumFormat {
    /**
     * 操作成功
     */
    SUCCESS("200", "操作成功"),
    /**
     * Error result code enum.
     */
    ERROR("-1", "操作失败"),
    /**
     * Biz error result code enum.
     */
    BIZ_ERROR("500", "通用业务异常"),
    /**
     * Unauthorized result code enum.
     */
    UNAUTHORIZED("401", "认证失败请重新登录"),
    /**
     * 登录过期。Token过期/Token失效
     */
    LOGIN_TIMEOUT("401", "登录过期"),
    /**
     * Forbidden result code enum.
     */
    FORBIDDEN("403", "权限不足无法访问"),
    /**
     * Param error result code enum.
     */
    PARAM_ERROR("2001", "参数错误"),
    /**
     * User login param error result code enum.
     */
    LOCK_ERROR("2002", "获取锁失败"),
    /**
     * Not found error result code enum.
     */
    NOT_FOUND_ERROR("2003", "数据不存在"),
    /**
     * User is disabled result code enum.
     */
    USER_IS_DISABLED("2004", "用户被禁用"),

    /**
     * Excel 解析失败
     */
    EXCEL_ANALYSIS_FAIL("2005", "Excel 解析失败"),

    /**
     * 文件下载失败
     */
    FILE_EXPORT_FAIL("2006", "文件下载失败"),
    /**
     * 用户名已注册
     */
    USERNAME_ALREADY_REGISTER("2007", "用户名已注册"),
    /**
     * 用户不存在
     */
    USER_NOT_EXIST("2008", "用户不存在"),
    /**
     * 请求过于频繁，请稍后再试
     */
    REQUEST_FREQUENTLY("2009", "请求过于频繁，请稍后再试");


    /**
     * 状态码
     */
    private final String code;

    /**
     * 状态信息
     */
    private final String message;

    ErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Gets result code.
     *
     * @return the result code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Gets result message.
     *
     * @return the result message
     */
    public String getMessage() {
        return this.message;
    }
}
