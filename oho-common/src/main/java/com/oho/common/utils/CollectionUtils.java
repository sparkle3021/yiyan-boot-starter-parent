package com.oho.common.utils;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 集合工具类
 *
 * @author Sparkler
 */
public class CollectionUtils extends CollectionUtil {


    /**
     * 按数量切割成多个list
     *
     * @param <T>      the type parameter
     * @param list     the list
     * @param quantity the quantity
     * @return the list
     */
    public static <T> List<List<T>> groupListByQuantity(List<T> list, int quantity) {
        if (list == null || list.size() == 0) {
            return new ArrayList<>();
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Wrong quantity.");
        }

        List<List<T>> wrapList = new ArrayList<>();
        int count = 0;
        while (count < list.size()) {
            wrapList.add(new ArrayList<>(list.subList(count, Math.min((count + quantity), list.size()))));
            count += quantity;
        }

        return wrapList;
    }

    /**
     * 交集
     *
     * @param <T>     the type parameter
     * @param maskOne the mask one
     * @param maskTwo the mask two
     * @return the retain
     */
    public static <T> List<T> setRetain(List<T> maskOne, List<T> maskTwo) {
        Set<T> set = new HashSet<>(maskOne);
        set.retainAll(maskTwo);
        return new ArrayList<>(set);
    }

    /**
     * 差集
     *
     * @param <T>     the type parameter
     * @param maskOne the mask one
     * @param maskTwo the mask two
     * @return the remove
     */
    public static <T> List<T> setRemove(List<T> maskOne, List<T> maskTwo) {
        Set<T> set = new HashSet<>(maskOne);
        maskTwo.forEach(set::remove);
        return new ArrayList<>(set);
    }

    /**
     * 补集
     *
     * @param <T>     the type parameter
     * @param maskOne the mask one
     * @param maskTwo the mask two
     * @return the repair
     */
    public static <T> List<T> setRepair(List<T> maskOne, List<T> maskTwo) {
        List<T> retain = setRetain(maskOne, maskTwo);
        // 减去交集
        List<T> remove = setRemove(maskOne, retain);
        return new ArrayList<>(remove);
    }

    /**
     * 并集去重
     *
     * @param <T>     the type parameter
     * @param maskOne the mask one
     * @param maskTwo the mask two
     * @return the all distinct
     */
    public static <T> List<T> setAllDistinct(List<T> maskOne, List<T> maskTwo) {
        Set<T> set = new HashSet<>();
        set.addAll(maskOne);
        set.addAll(maskTwo);
        return new ArrayList<>(set);
    }

    /**
     * 并集不去重
     *
     * @param <T>     the type parameter
     * @param maskOne the mask one
     * @param maskTwo the mask two
     * @return the all
     */
    public static <T> List<T> setAll(List<T> maskOne, List<T> maskTwo) {
        List<T> list = new ArrayList<>();
        JavaUtil.getJavaUtil()
                .acceptIfCondition(isNotEmpty(maskOne), maskOne, list::addAll)
                .acceptIfCondition(isNotEmpty(maskTwo), maskTwo, list::addAll);
        return list;
    }

    /**
     * List 转 Array
     *
     * @param <T>   the type parameter
     * @param data  the data
     * @param clazz the clazz
     * @return t [ ]
     */
    public static <T> T[] listToArray(List<T> data, Class<T> clazz) {
        T[] arr = ArrayUtil.newArray(clazz, data.size());
        arr = data.toArray(arr);
        return arr;
    }
}
