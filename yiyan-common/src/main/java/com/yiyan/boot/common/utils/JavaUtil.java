package com.yiyan.boot.common.utils;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

/**
 * java8函数式工具
 * <p>
 * if/else 等简单逻辑疯狂缩短代码
 *
 * @author Sparkler
 */
public final class JavaUtil {

    /**
     * 单例
     */
    private static volatile JavaUtil javaUtil;

    /**
     * 单例工具类获取
     *
     * @return java util
     */
    public static JavaUtil getJavaUtil() {
        if (javaUtil == null) {
            synchronized (JavaUtil.class) {
                if (javaUtil == null) {
                    javaUtil = new JavaUtil();
                }
            }
        }
        return javaUtil;
    }

    private JavaUtil() {
        super();
    }

    /**
     * 条件成立进行消费
     *
     * @param <T>       the type parameter
     * @param condition 条件
     * @param value     需要消费的参数
     * @param consumer  消费函数
     * @return java util
     */
    public <T> JavaUtil acceptIfCondition(boolean condition, T value, Consumer<T> consumer) {
        if (condition) {
            consumer.accept(value);
        }
        return this;
    }

    /**
     * 根据条件的成立与否进行参数消费
     *
     * @param <T>        the type parameter
     * @param condition  条件
     * @param trueValue  条件成立时消费参数
     * @param falseValue 条件不成立时消费参数
     * @param consumer   消费函数
     * @return java util
     */
    public <T> JavaUtil acceptDependCondition(boolean condition, T trueValue, T falseValue, Consumer<T> consumer) {

        consumer.accept(condition ? trueValue : falseValue);

        return this;
    }

    /**
     * 根据条件的成立决定消费函数
     *
     * @param <T>           the type parameter
     * @param condition     条件
     * @param value         消费的参数
     * @param consumerTrue  条件成立时消费参数
     * @param consumerFalse 条件不成立时消费函数
     * @return java util
     */
    public <T> JavaUtil consumeDependCondition(boolean condition, T value, Consumer<T> consumerTrue, Consumer<T> consumerFalse) {

        if (condition) {
            consumerTrue.accept(value);
        } else {
            consumerFalse.accept(value);
        }

        return this;
    }

    /**
     * 条件成立进行消费
     *
     * @param <T>       the type parameter
     * @param condition 条件
     * @param supplier  提供者函数返回值作为参数
     * @param consumer  消费函数
     * @return java util
     */
    public <T> JavaUtil acceptSupplierIfCondition(boolean condition, Supplier<T> supplier, Consumer<T> consumer) {
        if (condition) {
            consumer.accept(supplier.get());
        }
        return this;
    }

    /**
     * 消费参数如果提供者函数为空
     *
     * @param <T>      the type parameter
     * @param supplier 提供者函数作为判断入参
     * @param consumer 消费函数
     * @param defValue 消费入参
     * @return java util
     */
    public <T> JavaUtil acceptValueIfNull(Supplier<T> supplier, Consumer<T> consumer, T defValue) {
        return acceptIfCondition(supplier.get() == null, defValue, consumer);
    }

    /**
     * 参数值不为空则进行消费
     *
     * @param <T>      the type parameter
     * @param value    入参
     * @param consumer 消费函数
     * @return java util
     */
    public <T> JavaUtil acceptIfNotNull(T value, Consumer<T> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
        return this;
    }

    /**
     * 字符串入参不为空则进行消费
     *
     * @param value    入参
     * @param consumer 消费函数
     * @return java util
     */
    public JavaUtil acceptIfNotEmpty(String value, Consumer<String> consumer) {
        if (StringUtils.isNotBlank(value)) {
            consumer.accept(value);
        }
        return this;
    }

    /**
     * source不为null，则就行映射，并赋值
     *
     * @param <T>         the type parameter
     * @param <R>         the type parameter
     * @param source      入参值进行非空判断
     * @param mapFunction 对非空入参进行消费并返回消费参数
     * @param consumer    消费参数
     * @return java util
     */
    public <T, R> JavaUtil mapAndAcceptIfNonnull(T source, Function<T, R> mapFunction, Consumer<R> consumer) {
        if (source != null) {
            R apply = mapFunction.apply(source);
            consumer.accept(apply);
        }
        return this;
    }

    /**
     * List<对象>转换成 List<对象的某一个属性>，并赋值给别的类
     *
     * @param <T>         原始对象
     * @param <R>         对象里面的某个属性
     * @param list        入参
     * @param mapFunction 映射
     * @param consumer    消费
     * @return java util
     */
    public <T, R> JavaUtil mapAndAcceptIfNotEmpty(List<T> list, Function<T, R> mapFunction, Consumer<List<R>> consumer) {
        if (CollectionUtils.isNotEmpty(list)) {
            List<R> mapList = list.stream().map(mapFunction).collect(toList());
            consumer.accept(mapList);
        }
        return this;
    }


}
