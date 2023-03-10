package com.oho.common.constant;

import com.oho.common.enums.ErrorCodeEnum;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口响应码
 *
 * @author Sparkler
 * @createDate 2022/11/9
 */
@Data
@Builder
public class ErrorCodeConstant implements Serializable {

    private static final long serialVersionUID = -6269841958947880397L;

    /**
     * 状态码
     */
    private String code;

    /**
     * 状态信息
     */
    private String msg;

    /**
     * 默认成功
     */
    public final static ErrorCodeConstant SUCCESS = dispose(ErrorCodeEnum.SUCCESS);
    /**
     * 未登录或登录过期或登录验证失败 unauthorized
     */
    public final static ErrorCodeConstant UNAUTHORIZED = dispose(ErrorCodeEnum.UNAUTHORIZED);
    /**
     * 没有权限 forbidden
     */
    public final static ErrorCodeConstant FORBIDDEN = dispose(ErrorCodeEnum.FORBIDDEN);
    /**
     * 默认失败
     */
    public final static ErrorCodeConstant ERROR = dispose(ErrorCodeEnum.ERROR);
    /**
     * 通用业务异常
     */
    public final static ErrorCodeConstant BIZ_ERROR = dispose(ErrorCodeEnum.BIZ_ERROR);
    /**
     * 参数错误
     */
    public final static ErrorCodeConstant PARAM_ERROR = dispose(ErrorCodeEnum.PARAM_ERROR);
    /**
     * Excel 解析失败
     */
    public final static ErrorCodeConstant EXCEL_ANALYSIS_FAIL = dispose(ErrorCodeEnum.EXCEL_ANALYSIS_FAIL);


    private static ErrorCodeConstant dispose(ErrorCodeEnum codeEnum) {
        return ErrorCodeConstant.builder().code(codeEnum.getCode()).msg(codeEnum.getMessage()).build();
    }

    public ErrorCodeConstant(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
