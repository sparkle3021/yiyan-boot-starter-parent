package com.oho.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.oho.common.constant.BizConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Jwt token util.
 *
 * @author Sparkler
 * @createDate 2022 /11/24
 */
@Slf4j
@Getter
public class JWTUtil {

    private static volatile JWTUtil jwtUtil;
    /**
     * Token 过期时间，默认7200s
     */
    private long tokenExpire = BizConstant.DEFAULT_TOKEN_EXPIRE;
    /**
     * 刷新Token过期时间，默认7天，
     */
    private long refreshTokenExpire = BizConstant.DEFAULT_TOKEN_REFRESH_EXPIRE;
    /**
     * 密钥
     */
    private String secret = BizConstant.DEFAULT_SECRET;

    private JWTUtil() {
    }

    private JWTUtil(String secret) {
        this.secret = secret;
    }

    private JWTUtil(long tokenExpire) {
        this.tokenExpire = tokenExpire;
    }

    private JWTUtil(long tokenExpire, long refreshTokenExpire) {
        this.tokenExpire = tokenExpire;
        this.refreshTokenExpire = refreshTokenExpire;
    }

    private JWTUtil(long tokenExpire, String secret) {
        this.tokenExpire = tokenExpire;
        this.secret = secret;
    }

    private JWTUtil(long tokenExpire, long refreshTokenExpire, String secret) {
        this.tokenExpire = tokenExpire;
        this.refreshTokenExpire = refreshTokenExpire;
        this.secret = secret;
    }

    public static JWTUtil getInstance() {
        if (jwtUtil == null) {
            synchronized (JWTUtil.class) {
                if (jwtUtil == null) {
                    jwtUtil = new JWTUtil();
                }
            }
        }
        return jwtUtil;
    }

    public static JWTUtil getInstance(String secret) {
        if (jwtUtil == null) {
            synchronized (JWTUtil.class) {
                if (jwtUtil == null) {
                    jwtUtil = new JWTUtil(secret);
                }
            }
        }
        return jwtUtil;
    }

    public static JWTUtil getInstance(long tokenExpire) {
        if (jwtUtil == null) {
            synchronized (JWTUtil.class) {
                if (jwtUtil == null) {
                    jwtUtil = new JWTUtil(tokenExpire);
                }
            }
        }
        return jwtUtil;
    }

    public static JWTUtil getInstance(long tokenExpire, long refreshTokenExpire) {
        if (jwtUtil == null) {
            synchronized (JWTUtil.class) {
                if (jwtUtil == null) {
                    jwtUtil = new JWTUtil(tokenExpire, refreshTokenExpire);
                }
            }
        }
        return jwtUtil;
    }

    public static JWTUtil getInstance(long tokenExpire, long refreshTokenExpire, String secret) {
        if (jwtUtil == null) {
            synchronized (JWTUtil.class) {
                if (jwtUtil == null) {
                    jwtUtil = new JWTUtil(tokenExpire, refreshTokenExpire, secret);
                }
            }
        }
        return jwtUtil;
    }


    /**
     * The constant CLAIM_KEY_USERNAME.
     */
    public static final String CLAIM_KEY_USERNAME = "sub";
    /**
     * The constant CLAIM_KEY_CREATED.
     */
    public static final String CLAIM_KEY_CREATED = "created";

    /**
     * 根据负责生成JWT的token
     */
    private String generateToken(Map<String, Object> claims, Date expirationDate) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
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
     * 从token中获取登录用户名
     *
     * @param token the token
     * @return the user name from token
     */
    public String getUserNameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 验证token是否还有效
     *
     * @param token    客户端传入的token
     * @param username 用户名
     * @return the boolean
     */
    public boolean validateToken(String token, String username) {
        String usernameFromToken = getUserNameFromToken(token);
        return usernameFromToken.equals(username) && !isTokenExpired(token);
    }

    /**
     * Token 剩余有效时间.
     *
     * @param token the token
     * @return the long
     */
    public Long tokenValiditySeconds(String token) {
        Date expiredDateFromToken = getExpiredDateFromToken(token);
        return expiredDateFromToken.getTime() - System.currentTimeMillis();
    }

    /**
     * 判断token是否已经失效
     *
     * @param token the token
     * @return the boolean
     */
    public boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     *
     * @param token the token
     * @return the expired date from token
     */
    public Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 根据用户信息生成token
     *
     * @param username the username
     * @return the string
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, username);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims, generateExpirationDate());
    }

    /**
     * Generate refresh token string.
     *
     * @param username the username
     * @return the string
     */
    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, username);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims, new Date(System.currentTimeMillis() + refreshTokenExpire * 1000));
    }

    /**
     * 当原来的token没过期时是可以刷新的
     *
     * @param oldToken 带tokenHead的token
     * @return the string
     */
    public String refreshHeadToken(String oldToken) {
        if (StrUtil.isEmpty(oldToken)) {
            return null;
        }
        //token校验不通过
        Claims claims = getClaimsFromToken(oldToken);
        if (claims == null) {
            return null;
        }
        //如果token已经过期，不支持刷新
        if (isTokenExpired(oldToken)) {
            return null;
        }
        //如果token在30分钟之内刚刷新过，返回原token
        if (tokenRefreshJustBefore(oldToken, 30 * 60)) {
            return oldToken;
        } else {
            claims.put(CLAIM_KEY_CREATED, LocalDateTime.now());
            return generateToken(claims, generateExpirationDate());
        }
    }

    /**
     * 判断token在指定时间内是否刚刚刷新过
     *
     * @param token 原token
     * @param time  指定时间（秒）
     * @return the boolean
     */
    public boolean tokenRefreshJustBefore(String token, int time) {
        Claims claims = getClaimsFromToken(token);
        Date created = claims.get(CLAIM_KEY_CREATED, Date.class);
        Date refreshDate = new Date();
        //刷新时间在创建时间的指定时间内
        return refreshDate.after(created) && refreshDate.before(DateUtil.offsetSecond(created, time));
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
