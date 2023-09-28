package com.yiyan.boot.common.utils;


import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * 排序工具类
 * </p>
 * 必须实现Comparable接口
 *
 * @author Alex
 * @createDate 2023 -05-05 10:17
 */
public class SortUtils {
    private SortUtils() {
    }

    /**
     * Swap. 交换数组中的两个元素
     *
     * @param <T> the type parameter
     * @param arr 目标数组
     * @param i   当前值
     * @param j   目标值
     */
    public static <T> void swap(T[] arr, int i, int j) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * 冒泡排序
     *
     * @param <T> 泛型类型
     * @param arr 待排序数组
     */
    public static <T extends Comparable<T>> void bubbleSort(T[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int len = arr.length;
        boolean flag = true;
        for (int i = 0; i < len - 1 && flag; i++) {
            flag = false;
            for (int j = 0; j < len - 1 - i; j++) {
                if (arr[j].compareTo(arr[j + 1]) > 0) {
                    swap(arr, j, j + 1);
                    flag = true;
                }
            }
        }
    }

    /**
     * 选择排序
     *
     * @param <T> 泛型类型
     * @param arr 待排序数组
     */
    public static <T extends Comparable<T>> void selectionSort(T[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int len = arr.length;
        int minIndex;
        for (int i = 0; i < len - 1; i++) {
            minIndex = i;
            for (int j = i; j < len; j++) {
                if (arr[j].compareTo(arr[minIndex]) < 0) {
                    minIndex = j;
                }
            }
            swap(arr, i, minIndex);
        }
    }

    /**
     * 插入排序
     *
     * @param <T> 泛型类型
     * @param arr 待排序数组
     */
    public static <T extends Comparable<T>> void insertionSort(T[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int len = arr.length;
        T tmp;
        for (int i = 1; i < len; i++) {
            tmp = arr[i];
            int j = i - 1;
            while (j >= 0 && tmp.compareTo(arr[j]) < 0) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = tmp;
        }
    }

    /**
     * 希尔排序
     *
     * @param <T> 泛型类型
     * @param arr 待排序数组
     */
    public static <T extends Comparable<T>> void shellSort(T[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int len = arr.length;
        T tmp;
        for (int gap = len / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < len; i++) {
                tmp = arr[i];
                int j = i - gap;
                while (j >= 0 && tmp.compareTo(arr[j]) < 0) {
                    arr[j + gap] = arr[j];
                    j -= gap;
                }
                arr[j + gap] = tmp;
            }
        }
    }

    /**
     * 快速排序
     *
     * @param <T>  泛型类型
     * @param arr  待排序数组
     * @param low  排序起始位置
     * @param high 排序结束位置
     */
    public static <T extends Comparable<T>> void quickSort(T[] arr, int low, int high) {
        if (arr == null || arr.length < 2) {
            return;
        }
        if (low < high) {
            int pivot = partition(arr, low, high);
            quickSort(arr, low, pivot - 1);
            quickSort(arr, pivot + 1, high);
        }
    }

    private static <T extends Comparable<T>> int partition(T[] arr, int low, int high) {
        T pivot = arr[low];
        while (low < high) {
            while (low < high && arr[high].compareTo(pivot) >= 0) {
                high--;
            }
            arr[low] = arr[high];
            while (low < high && arr[low].compareTo(pivot) < 0) {
                low++;
            }
            arr[high] = arr[low];
        }
        arr[low] = pivot;
        return low;
    }

    /**
     * 归并排序
     *
     * @param <T> 泛型
     * @param arr 待排序数组
     * @return the t [ ]
     */
    public static <T extends Comparable<T>> T[] mergeSort(T[] arr) {
        if (arr == null || arr.length < 2) {
            return arr;
        }
        int mid = arr.length / 2;
        T[] left = Arrays.copyOfRange(arr, 0, mid);
        T[] right = Arrays.copyOfRange(arr, mid, arr.length);
        return merge(mergeSort(left), mergeSort(right));
    }

    private static <T extends Comparable<T>> T[] merge(T[] left, T[] right) {
        T[] result = (T[]) Array.newInstance(left.getClass().getComponentType(), left.length + right.length);
        int i = 0;
        while (left.length > 0 && right.length > 0) {
            if (left[0].compareTo(right[0]) <= 0) {
                result[i++] = left[0];
                left = Arrays.copyOfRange(left, 1, left.length);
            } else {
                result[i++] = right[0];
                right = Arrays.copyOfRange(right, 1, right.length);
            }
        }

        while (left.length > 0) {
            result[i++] = left[0];
            left = Arrays.copyOfRange(left, 1, left.length);
        }

        while (right.length > 0) {
            result[i++] = right[0];
            right = Arrays.copyOfRange(right, 1, right.length);
        }
        return result;
    }
}
