package com.yiyan.boot.common.utils;

import com.yiyan.boot.common.utils.encrypt.RASUtil;
import com.yiyan.boot.common.utils.encrypt.RsaKeyPair;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

@Slf4j
class JWTUtilTest {
    /**
     * 初始化参数
     */
    static {
        JWTUtil.initialize("issuer", "secretKey", 10000L, "header", null, SignatureAlgorithm.HS256, "audience");
    }

    @Test
    void testGenerateToken() {
        String result = JWTUtil.generateToken("subject");
        log.info("generateToken-1: {}", result);
    }

    @Test
    void testGenerateToken2() {
        String result = JWTUtil.generateToken("subject", 11111L);
        log.info("generateToken-2: {}", result);
    }

    @Test
    void testGenerateToken3() {
        String result = JWTUtil.generateToken("subject", new HashMap<String, Object>() {{
            put("key", "value");
        }}, 10000L);
        log.info("generateToken-3: {}", result);
    }

    @Test
    void testGenerateToken4() {
        String result = JWTUtil.generateToken("subject", new HashMap<String, Object>() {{
            put("key", "value");
        }});
        log.info("generateToken-4: {}", result);
    }

    @Test
    void testGenerateToken5() {
        String result = JWTUtil.generateToken(new HashMap<String, Object>() {{
            put("key", "value");
        }});
        log.info("generateToken-5: {}", result);
    }

    @Test
    void testGenerateToken6() {
        String result = JWTUtil.generateToken(new HashMap<String, Object>() {{
            put("key", "value");
        }}, 10000L);
        log.info("generateToken-6: {}", result);
    }

    @Test
    void testEncodeAndDecodeToken() {
        RsaKeyPair rsaKeyPair = RASUtil.generateRasKeyPair();
        log.info("RsaKeyPair: {}", rsaKeyPair);
        String token
                = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzdWJqZWN0IiwiaXNzIjoiaXNzdWVyIiwiYXVkIjoiYXVkaWVuY2UiLCJleHAiOjE2MzUwNjYwNjAsImlhdCI6MTYzNTA2NjA2MCwianRpIjoiZjQ4ZjQ5ZjItZjQ5Zi00ZjQ5LWE0ZjQtZjQ5ZjQ5ZjQ";
        // 计算用时
        long start = System.currentTimeMillis();
        String encodeToken = JWTUtil.encodeToken(token, rsaKeyPair.getPublicKey());
        long end = System.currentTimeMillis();
        log.info("encode use time: {}ms", (end - start));
        log.info("encodeToken: {}", encodeToken);
        long start2 = System.currentTimeMillis();
        String decodeToken = JWTUtil.decodeToken(encodeToken, rsaKeyPair.getPrivateKey());
        long end2 = System.currentTimeMillis();
        log.info("decode use time: {}ms", (end2 - start2));
        log.info("decodeToken: {}", decodeToken);
    }

}