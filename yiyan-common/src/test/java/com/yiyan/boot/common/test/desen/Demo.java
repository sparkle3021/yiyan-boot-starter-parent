package com.yiyan.boot.common.test.desen;

import cn.hutool.core.util.DesensitizedUtil;
import com.yiyan.boot.common.utils.desensitization.Desensitization;
import lombok.Data;

/**
 * @author MENGJIAO
 * @createDate 2023-10-19 0019 下午 01:11
 */
@Data
public class Demo {
    @Desensitization(type = DesensitizedUtil.DesensitizedType.CHINESE_NAME)
    private String name;

    @Desensitization(type = DesensitizedUtil.DesensitizedType.ID_CARD)
    private String idCard;

    @Desensitization(type = DesensitizedUtil.DesensitizedType.MOBILE_PHONE)
    private String phone;

    @Desensitization(type = DesensitizedUtil.DesensitizedType.ADDRESS)
    private String address;

    @Desensitization(type = DesensitizedUtil.DesensitizedType.EMAIL)
    private String email;

    @Desensitization(type = DesensitizedUtil.DesensitizedType.PASSWORD)
    private String password;

    @Desensitization(type = DesensitizedUtil.DesensitizedType.BANK_CARD)
    private String bankCard;
}
