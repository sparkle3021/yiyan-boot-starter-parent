package com.oho.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Bean属性拷贝工具类
 *
 * @author Sparkler
 * @createDate 2022 /12/11
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {


    /**
     * 简单属性拷贝
     *
     * @param <S>    the type parameter
     * @param <T>    the type parameter
     * @param source 数据源
     * @param target 目标类::new
     * @return the t
     */
    public static <S, T> T copyProperties(S source, Supplier<T> target) {
        T t = target.get();
        copyProperties(source, t);
        return t;
    }


    /**
     * 简单属性拷贝，通过反序列化方式实现Copy，适用于属性名一致的对象
     *
     * @param <S>         the type parameter
     * @param <T>         the type parameter
     * @param source      the source
     * @param targetClass the target class
     * @return the t
     */
    public static <S, T> T copyProperties(S source, Class<T> targetClass) {
        String sourceJson = JsonUtils.toJson(source);
        return JsonUtils.toBean(sourceJson, targetClass);
    }

    /**
     * 简单属性拷贝 -- 指定属性复制
     *
     * @param <S>      the type parameter
     * @param <T>      the type parameter
     * @param source   数据源
     * @param target   目标类::new
     * @param callBack the call back
     * @return the t
     */
    public static <S, T> T copyProperties(S source, Supplier<T> target, BeanCopyUtilCallBack<S, T> callBack) {
        T t = target.get();
        copyProperties(source, t);
        if (callBack != null) {
            callBack.callBack(source, t);
        }
        return t;
    }

    /**
     * 集合数据的拷贝
     *
     * @param <S>     the type parameter
     * @param <T>     the type parameter
     * @param sources : 数据源类
     * @param target  :  目标类::new(eg: TargetDTO::new)
     * @return list list
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        return copyListProperties(sources, target, null);
    }


    /**
     * 带回调函数的集合数据的拷贝（可自定义字段拷贝规则）
     *
     * @param <S>      the type parameter
     * @param <T>      the type parameter
     * @param sources  :  数据源类
     * @param target   :   目标类::new(eg: TargetDTO::new)
     * @param callBack : 回调函数
     * @return list list
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target, BeanCopyUtilCallBack<S, T> callBack) {
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = target.get();
            copyProperties(source, t);
            if (callBack != null) {
                // 回调
                callBack.callBack(source, t);
            }
            list.add(t);
        }
        return list;
    }

    /**
     * The interface Bean copy util call back.
     *
     * @param <S> the type parameter
     * @param <T> the type parameter
     */
    @FunctionalInterface
    public interface BeanCopyUtilCallBack<S, T> {

        /**
         * 定义默认回调方法
         *
         * @param s the s
         * @param t the t
         */
        void callBack(S s, T t);
    }
}
