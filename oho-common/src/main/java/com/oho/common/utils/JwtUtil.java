package com.oho.common.utils;

import com.oho.common.constant.BizConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Jwt工具类
 *
 * @author MENGJIAO
 * @createDate 2022-11-24
 */
@Slf4j
@Data
public class JwtUtil {
    /**
     * Token 过期时间，默认7200s
     */
    private long tokenExpire = BizConstant.DEFAULT_TOKEN_EXPIRE;
    /**
     * 密钥
     */
    private String secret = BizConstant.DEFAULT_SECRET;
    /**
     * ISSUER
     */
    private String issuer = "jwt_issuer";
    /**
     * The constant CLAIM_KEY_USERNAME.
     */
    public static final String CLAIM_KEY_USERNAME = "sub";
    /**
     * The constant CLAIM_KEY_CREATED.
     */
    public static final String CLAIM_KEY_CREATED = "created";

    private JwtUtil() {
    }

    private JwtUtil(String secret, long tokenExpire, String issuer) {
        this.secret = secret;
        this.tokenExpire = tokenExpire;
        this.issuer = issuer;
    }


    public static JwtUtil getInstance() {
        return new JwtUtil();
    }

    public static JwtUtil getInstance(String secret, long tokenExpire, String issuer) {
        return new JwtUtil(secret, tokenExpire, issuer);
    }

    /**
     * 根据负责生成JWT的token
     */
    private String generateToken(Map<String, Object> claims, Date expirationDate) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .setIssuer(issuer)
                .compact();
    }

    /**
     * 从token中获取JWT中的负载
     *
     * @param token the token
     * @return the claims from token
     */
    public Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.error("JWT格式验证失败:{}", token);
        }
        return claims;
    }

    /**
     * 生成token的过期时间
     *
     * @return the date
     */
    public Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + tokenExpire * 1000);
    }

    /**
     * 从token中获取业务Key
     *
     * @param token the token
     * @return the user name from token
     */
    public String getUserNameFromToken(String token) {
        String bizKey;
        try {
            Claims claims = getClaimsFromToken(token);
            bizKey = claims.getSubject();
        } catch (Exception e) {
            bizKey = null;
        }
        return bizKey;
    }

    /**
     * 验证token是否还有效
     *
     * @param token 客户端传入的token
     * @param key   业务key
     * @return 有效返回true，失效返回false
     */
    public boolean validateToken(String token, String key) {
        String usernameFromToken = getUserNameFromToken(token);
        return usernameFromToken.equals(key) && !isTokenExpired(token);
    }

    /**
     * Token 剩余有效时间.
     *
     * @param token 客户端传入的token
     * @return 返回剩余时间
     */
    public Long tokenValiditySeconds(String token) {
        Date expiredDateFromToken = getExpiredDateFromToken(token);
        return expiredDateFromToken.getTime() - System.currentTimeMillis();
    }

    /**
     * 判断token是否已经失效
     *
     * @param token 客户端传入Token
     * @return 有效返回true，失效返回false
     */
    public boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     *
     * @param token 客户端传入token
     * @return 返回过期时间
     */
    public Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 根据用户信息生成token
     *
     * @param key 业务key
     * @return token
     */
    public String generateToken(String key) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, key);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims, generateExpirationDate());
    }

    /**
     * 获取Token剩余有效期，单位秒
     *
     * @param token the token
     * @return token remain time
     */
    public Long getTokenRemainTime(String token) {
        Claims claims = getClaimsFromToken(token);
        Date expiration = claims.getExpiration();
        return expiration.getTime() - System.currentTimeMillis();
    }
}
