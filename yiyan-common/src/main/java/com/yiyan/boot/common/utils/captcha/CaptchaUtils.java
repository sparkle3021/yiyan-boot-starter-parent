package com.yiyan.boot.common.utils.captcha;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.lang.UUID;
import com.yiyan.boot.common.utils.captcha.generator.MixStrGenerator;
import lombok.Data;
import lombok.Getter;

/**
 * 图片验证码工具类
 * PS: 返回的验证码为base64格式 及 校验值
 *
 * @author MENGJIAO
 * @createDate 2023-10-07 下午 11:28
 */
public class CaptchaUtils {

    /**
     * 默认图片宽度
     */
    private static final int DEFAULT_IMG_WIDTH = 100;
    /**
     * 默认图片高度
     */
    private static final int DEFAULT_IMG_HEIGHT = 30;
    /**
     * 默认验证码长度
     */
    private static final int DEFAULT_VERIFY_LENGTH = 4;

    private CaptchaUtils() {
    }

    /**
     * 生成纯数字的验证码
     *
     * @param imgWidth     图片宽度
     * @param imgHeight    图片高度
     * @param verifyLength 验证码长度
     * @return 验证码
     */
    public static CaptchaData numberCaptcha(int imgWidth, int imgHeight, int verifyLength) {
        RandomGenerator randomGenerator = new RandomGenerator("0123456789", verifyLength);
        // 自定义纯数字的验证码（随机4位数字，可重复）
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(imgWidth, imgHeight);
        lineCaptcha.setGenerator(randomGenerator);
        // 生成验证码
        lineCaptcha.createCode();
        return new CaptchaData("data:image/png;base64," + lineCaptcha.getImageBase64(), lineCaptcha.getCode());
    }

    /**
     * 生成混合字符串(数字+字母)的验证码
     *
     * @param imgWidth     图片宽度
     * @param imgHeight    图片高度
     * @param verifyLength 验证码长度
     * @return 验证码
     */
    public static CaptchaData mixStrCaptcha(int imgWidth, int imgHeight, int verifyLength) {
        MixStrGenerator mixStrGenerator = new MixStrGenerator(verifyLength);
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(imgWidth, imgHeight);
        lineCaptcha.setGenerator(mixStrGenerator);
        lineCaptcha.createCode();
        return new CaptchaData("data:image/png;base64," + lineCaptcha.getImageBase64(), lineCaptcha.getCode());
    }

    /**
     * 生成数学计算的验证码
     *
     * @param imgWidth  图片宽度
     * @param imgHeight 图片高度
     * @return 验证码
     */
    public static CaptchaData mathCaptcha(int imgWidth, int imgHeight) {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(imgWidth, imgHeight);
        // 设置MathGenerator验证码生成器
        lineCaptcha.setGenerator(new MathGenerator());
        // 生成验证码
        lineCaptcha.createCode();
        String computeResult = "";
        String code = lineCaptcha.getCode().substring(0, lineCaptcha.getCode().length() - 1);
        code = code.replace(" ", "");
        // 计算结果
        if (code.contains("*")) {
            String[] split = code.split("\\*");
            computeResult = String.valueOf(Integer.parseInt(split[0]) * Integer.parseInt(split[1]));
        } else if (code.contains("+")) {
            String[] split = code.split("\\+");
            computeResult = String.valueOf(Integer.parseInt(split[0]) + Integer.parseInt(split[1]));
        } else if (code.contains("-")) {
            String[] split = code.split("-");
            computeResult = String.valueOf(Integer.parseInt(split[0]) - Integer.parseInt(split[1]));
        } else {
            String[] split = code.split("/");
            computeResult = String.valueOf(Integer.parseInt(split[0]) / Integer.parseInt(split[1]));
        }
        return new CaptchaData("data:image/png;base64," + lineCaptcha.getImageBase64(), computeResult);
    }

    /**
     * 生成验证码
     *
     * @param captchaType  验证码类型
     * @param imgWidth     图片宽度
     * @param imgHeight    图片高度
     * @param verifyLength 验证码长度
     * @return 验证码
     */
    public static CaptchaData generatorCaptcha(CaptchaType captchaType, int imgWidth, int imgHeight, int... verifyLength) {
        if (captchaType == CaptchaType.NUMBER) {
            return numberCaptcha(imgWidth, imgHeight, verifyLength[0]);
        } else if (captchaType == CaptchaType.MIX_STR) {
            return mixStrCaptcha(imgWidth, imgHeight, verifyLength[0]);
        }
        return mathCaptcha(DEFAULT_IMG_WIDTH, DEFAULT_IMG_HEIGHT);
    }

    /**
     * 生成验证码
     *
     * @param captchaType  验证码类型
     * @param verifyLength 验证码长度
     * @return 验证码
     */
    public static CaptchaData generatorCaptcha(CaptchaType captchaType, int... verifyLength) {
        if (captchaType == CaptchaType.NUMBER) {
            return numberCaptcha(DEFAULT_IMG_WIDTH, DEFAULT_IMG_HEIGHT, verifyLength[0]);
        } else if (captchaType == CaptchaType.MIX_STR) {
            return mixStrCaptcha(DEFAULT_IMG_WIDTH, DEFAULT_IMG_HEIGHT, verifyLength[0]);
        }
        return mathCaptcha(DEFAULT_IMG_WIDTH, DEFAULT_IMG_HEIGHT);
    }

    /**
     * 生成验证码
     *
     * @param captchaType 验证码类型
     * @return 验证码
     */
    public static CaptchaData generatorCaptcha(CaptchaType captchaType) {
        if (captchaType == CaptchaType.NUMBER) {
            return numberCaptcha(DEFAULT_IMG_WIDTH, DEFAULT_IMG_HEIGHT, DEFAULT_VERIFY_LENGTH);
        } else if (captchaType == CaptchaType.MIX_STR) {
            return mixStrCaptcha(DEFAULT_IMG_WIDTH, DEFAULT_IMG_HEIGHT, DEFAULT_VERIFY_LENGTH);
        }
        return mathCaptcha(DEFAULT_IMG_WIDTH, DEFAULT_IMG_HEIGHT);
    }

    public static void main(String[] args) {
        CaptchaData captchaData = generatorCaptcha(CaptchaType.MIX_STR, 4);
        System.out.println(captchaData);
    }

    @Getter
    public enum CaptchaType {
        /**
         * 数字验证码
         */
        NUMBER,
        /**
         * 数学计算验证码
         */
        MATH,
        /**
         * 混合字符串验证码
         */
        MIX_STR;
    }

    /**
     * 验证码数据
     */
    @Data
    public static class CaptchaData {
        /**
         * 验证码唯一标识，用于校验
         */
        String identification = UUID.fastUUID().toString(Boolean.TRUE);
        /**
         * base64格式的图片验证码，用于展示
         */
        String captchaImg;
        /**
         * 验证码内容，用于校验
         */
        String captchaCode;

        public CaptchaData(String captchaImg, String captchaCode) {
            this.captchaImg = captchaImg;
            this.captchaCode = captchaCode;
        }
    }
}
