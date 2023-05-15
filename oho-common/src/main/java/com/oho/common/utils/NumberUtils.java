package com.oho.common.utils;

/**
 * 数学工具类
 *
 * @author MENGJIAO
 * @createDate 2023-05-05 9:25
 */
public class NumberUtils {
    private NumberUtils() {
    }

    /**
     * 是否为数字
     *
     * @param str 待判断的字符串
     * @return the boolean
     */
    public static boolean isNumber(String str) {
        return str.matches("-?[0-9]+.*[0-9]*");
    }

    /**
     * 是否为整数
     *
     * @param str 待判断的字符串
     * @return the boolean
     */
    public static boolean isInteger(String str) {
        return str.matches("-?[0-9]+");
    }

    /**
     * 是否为浮点数
     *
     * @param str 待判断的字符串
     * @return the boolean
     */
    public static boolean isDouble(String str) {
        return str.matches("-?[0-9]+.[0-9]+");
    }

    /**
     * 是否为质数
     *
     * @param num 待判断的数字
     * @return the boolean
     */
    public static boolean isPrime(int num) {
        if (num < 2) {
            return false;
        }
        if (num == 2 || num == 3) {
            return true;
        }
        if (num % 6 != 1 && num % 6 != 5) {
            return false;
        }
        int tmp = (int) Math.sqrt(num);
        for (int i = 5; i <= tmp; i += 6) {
            if (num % i == 0 || num % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 进制转换
     *
     * @param num       待转换的数
     * @param fromRadix 当前进制
     * @param toRadix   目标进制
     * @return the string
     */
    public static String convert(String num, int fromRadix, int toRadix) {
        return Integer.toString(Integer.parseInt(num, fromRadix), toRadix);
    }

    /**
     * 任意进制转二进制
     *
     * @param num       待转换的数
     * @param fromRadix 当前进制
     * @return the string
     */
    public static String toBinary(String num, int fromRadix) {
        return convert(num, fromRadix, 2);
    }

    /**
     * 二进制转任意进制
     *
     * @param num     待转换的数
     * @param toRadix 目标进制
     * @return the string
     */
    public static String fromBinary(String num, int toRadix) {
        return convert(num, 2, toRadix);
    }

    /**
     * 二进制转int
     *
     * @param num 二进制字符串
     * @return the int
     */
    public static int binaryToInt(String num) {
        return Integer.parseInt(num, 2);
    }

    /**
     * 二进制转long
     *
     * @param num 二进制字符串
     * @return the long
     */
    public static long binaryToLong(String num) {
        return Long.parseLong(num, 2);
    }

    /**
     * 比较两个值的大小
     *
     * @param x the x
     * @param y the y
     * @return the int
     */
    public static int compare(long x, long y) {
        return Long.compare(x, y);
    }

    /**
     * 加权平均数
     *
     * @param values  the values
     * @param weights the weights
     * @return the double
     */
    public static double weightedAverage(double[] values, double[] weights) {
        double sum = 0;
        double sumWeight = 0;
        for (int i = 0; i < values.length; i++) {
            sum += values[i] * weights[i];
            sumWeight += weights[i];
        }
        return sum / sumWeight;
    }

    /**
     * 生成不重复随机数 根据给定的最小数字和最大数字，以及随机数的个数，产生指定的不重复的数组。
     *
     * @param min 最小值
     * @param max 最大值
     * @param n   生产随机数的个数
     * @return the int [ ]
     */
    public static int[] randomCommon(int min, int max, int n) {
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        int[] result = new int[n];
        int count = 0;
        while (count < n) {
            // Math.random()返回的是double类型，需要强制转换类型
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (num == result[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                result[count] = num;
                count++;
            }
        }
        return result;
    }


}
