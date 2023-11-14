package com.yiyan.boot.common.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.yiyan.boot.common.constant.ErrorCodeConstant;
import com.yiyan.boot.common.exception.BizException;
import com.yiyan.boot.common.model.result.Result;
import com.yiyan.boot.common.utils.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 全局异常处理
 *
 * @author MengJiao
 * @createDate 2022-11-23
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Resource
    private LocalValidatorFactoryBean localValidatorFactoryBean;

    /**
     * 处理自定义的业务异常
     *
     * @param req the req
     * @param e   the e
     * @return result
     */
    @ExceptionHandler(value = BizException.class)
    public Result<BizException> bizExceptionHandler(HttpServletRequest req, BizException e) {
        log.error("[ {} ] {} 请求异常: {}", req.getMethod(), req.getRequestURL(), e.getErrorMsg());
        return Result.error(e.getErrorCode(), e.getErrorMsg());
    }

    /**
     * 参数异常信息返回
     *
     * @param req the req
     * @param e   the e
     * @return result
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<JsonNode> methodArgumentNotValidExceptionHandler(HttpServletRequest req, MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        log.error("[ {} ] {} 请求参数校验错误", req.getMethod(), req.getRequestURL());
        Map<String, String> paramExceptionInfo = new TreeMap<>();
        for (ObjectError objectError : allErrors) {
            FieldError fieldError = (FieldError) objectError;
            log.error("参数 {} = {} 校验错误：{}", fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
            paramExceptionInfo.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return Result.error(ErrorCodeConstant.PARAM_ERROR, JsonUtils.toJson(paramExceptionInfo));
    }

    /**
     * 处理其他异常
     *
     * @param req the req
     * @param e   the e
     * @return result
     */
    @ExceptionHandler(value = Exception.class)
    public Result<Exception> exceptionHandler(HttpServletRequest req, Exception e) {
        log.error("[ {} ] {} 未定义异常: {}", req.getMethod(), req.getRequestURL(), e.getMessage());
        return Result.error(ErrorCodeConstant.ERROR, e.getMessage());
    }
}

