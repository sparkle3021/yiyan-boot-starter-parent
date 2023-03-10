package com.oho.common.utils;

import com.oho.common.exception.ValidationException;

import java.util.Collection;
import java.util.Objects;

/**
 * 校验工具类
 *
 * @author baiyan
 * @date 2020/11/13
 */
public class ValidationUtils {

    public static void sneakyThrow(String code, Object... params) {
        throw new ValidationException(code, params);
    }

    public static void isTrue(boolean expect, String code, Object... params) {
        if (!expect) {
            throw ValidationException.of(code, params);
        }
    }

    public static void isFalse(boolean expect, String code, Object... params) {
        isTrue(!expect, code, params);
    }

    public static void equals(String first, String second, String code, Object... params) {
        isTrue(first.equals(second), code, params);
    }

    public static void nil(Object object, String code, Object... params) {
        isTrue(Objects.isNull(object), code, params);
    }

    public static void notNull(Object object, String code, Object... params) {
        isTrue(Objects.nonNull(object), code, params);
    }

    public static void equals(Object first, Object second, String code, Object... params) {
        isTrue(Objects.equals(first, second), code, params);
    }

    public static void notEquals(Object first, Object second, String code, Object... params) {
        isTrue(!Objects.equals(first, second), code, params);
    }

    public static void empty(Collection collection, String code, Object... params) {
        isTrue(CollectionUtils.isEmpty(collection), code, params);
    }

    public static void notEmpty(Collection collection, String code, Object... params) {
        isTrue(CollectionUtils.isNotEmpty(collection), code, params);
    }

    public static void blank(String str, String code, Object... params) {
        isTrue(StringUtils.isBlank(str), code, params);
    }

    public static void notBlank(String str, String code, Object... params) {
        isTrue(StringUtils.isNotBlank(str), code, params);
    }

}
