package com.yiyan.boot.common.test.desen;

import com.yiyan.boot.common.utils.json.JsonUtils;

/**
 * 脱敏测试
 *
 * @author MENGJIAO
 * @createDate 2023-10-19 0019 下午 01:11
 */
public class DesenTest {
    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.setName("张三");
        demo.setIdCard("123456789012345678");
        demo.setPhone("12345678901");
        demo.setAddress("北京市朝阳区");
        demo.setEmail("1223@email.com");
        demo.setPassword("123456");
        demo.setBankCard("1234567890123456789");

        System.out.println(JsonUtils.toJson(demo));
    }
}
