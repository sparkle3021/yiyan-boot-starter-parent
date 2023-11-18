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
    SUCCESS("200", "操作成功"),
    ERROR("-1", "操作失败"),
    BIZ_ERROR("500", "通用业务异常"),
    UNAUTHORIZED("401", "认证失败请重新登录"),
    LOGIN_TIMEOUT("401", "登录过期"),
    FORBIDDEN("403", "权限不足无法访问"),
    PARAM_ERROR("2001", "参数错误"),
    LOCK_ERROR("2002", "获取锁失败"),
    NOT_FOUND_ERROR("2003", "数据不存在"),
    LOCAL_CACHE_NOT_EXIST("2004", "本地缓存不存在"),
    LOCAL_CACHE_EXIST("2005", "本地缓存已存在"),
    EXCEL_ANALYSIS_FAIL("2006", "Excel 解析失败"),
    FILE_EXPORT_FAIL("2007", "文件下载失败"),
    USERNAME_ALREADY_REGISTER("2008", "用户名已注册"),
    USER_NOT_EXIST("2009", "用户不存在"),
    REQUEST_FREQUENTLY("2010", "请求过于频繁，请稍后再试");

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

}
