package com.yiyan.boot.common.utils.captcha.generator;

import cn.hutool.captcha.generator.AbstractGenerator;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 混合字符串验证码生成器
 *
 * @author MENGJIAO
 * @createDate 2023-10-08 上午 12:37
 */
public class MixStrGenerator extends AbstractGenerator {

    private static final long serialVersionUID = 1L;

    public MixStrGenerator(int count) {
        super(count);
    }

    @Override
    public String generate() {
        return RandomUtil.randomString(this.length);
    }

    @Override
    public boolean verify(String code, String userInputCode) {
        if (StrUtil.isNotBlank(userInputCode)) {
            return StrUtil.equalsIgnoreCase(code, userInputCode);
        }
        return false;
    }
}
