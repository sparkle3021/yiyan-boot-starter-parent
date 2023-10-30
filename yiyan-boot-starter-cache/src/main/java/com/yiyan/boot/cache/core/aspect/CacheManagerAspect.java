package com.yiyan.boot.cache.core.aspect;


import cn.hutool.core.util.StrUtil;
import com.yiyan.boot.cache.autoconfigure.properties.MultiLayerCacheProperties;
import com.yiyan.boot.cache.core.annotation.CacheDelete;
import com.yiyan.boot.cache.core.annotation.CachePut;
import com.yiyan.boot.cache.core.annotation.Cached;
import com.yiyan.boot.cache.core.service.CacheService;
import com.yiyan.boot.common.exception.Asserts;
import com.yiyan.boot.common.utils.ObjectUtils;
import com.yiyan.boot.common.utils.SpElUtils;
import com.yiyan.boot.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * AOP切面，缓存拦截处理
 */
@Aspect
@Slf4j
@ConditionalOnProperty(name = "multi.cache.enable", havingValue = "true")
public class CacheManagerAspect {

    private CacheService cacheService;
    private MultiLayerCacheProperties cacheConfigProperties;

    public CacheManagerAspect(CacheService cacheService, MultiLayerCacheProperties cacheConfigProperties) {
        this.cacheService = cacheService;
        this.cacheConfigProperties = cacheConfigProperties;
    }

    @Pointcut("@annotation(com.yiyan.boot.cache.core.annotation.Cached)")
    public void executionOfCachedMethod() {
    }

    @Pointcut("@annotation(com.yiyan.boot.cache.core.annotation.CachePut)")
    public void executionOfCachePutMethod() {
    }

    @Pointcut("@annotation(com.yiyan.boot.cache.core.annotation.CacheDelete)")
    public void executionOfCacheDeleteMethod() {
    }

    public static long buildCacheKey(Object... args) {
        StringBuilder key = new StringBuilder(":");
        for (Object obj : args) {
            if (obj != null) {
                key.append(obj.toString()).append(":");
            }
        }
        return DigestUtils.sha1Hex(key.toString()).hashCode();
    }

    /**
     * 获取缓存
     * ps: 如果没有则执行方法，并将结果缓存
     *
     * @param proceedingJoinPoint 切点
     * @return Object
     * @throws Throwable 异常
     */
    @Around("executionOfCachedMethod()")
    public Object getAndSaveInCache(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 如果没有开启缓存，直接执行方法
        if (!cacheConfigProperties.isEnable()) {
            return callActualMethod(proceedingJoinPoint);
        }
        // 返回值
        Object returnObject = null;
        // 获取注解
        Cached cachedAnnotation = null;
        // 缓存key
        Object cacheKey = null;
        try {
            cachedAnnotation = getAnnotation(proceedingJoinPoint, Cached.class);
            // 如果没有指定key表达式，则使用默认的key生成策略
            if (StringUtils.isEmpty(cachedAnnotation.cacheKay())) {
                cacheKey = buildCacheKey(proceedingJoinPoint.getArgs());
            } else {
                cacheKey = parseAndGetCacheKeyFromExpression(generateOriginalCachedKey(proceedingJoinPoint, cachedAnnotation));
            }
            // 从缓存中获取数据
            returnObject = cacheService.getFromCache(cachedAnnotation.cacheName(), cacheKey);
        } catch (Exception e) {
            log.error("[缓存] - [获取缓存] - 异常 ：" + e.getMessage(), e);
        }
        // 如果缓存中有数据，则直接返回
        if (returnObject != null) {
            return returnObject;
        }
        // 如果缓存中没有数据，则执行方法
        returnObject = callActualMethod(proceedingJoinPoint);
        // 如果方法返回值不为空，则保存到缓存中
        if (returnObject != null) {
            try {
                if (cachedAnnotation.isAsync()) {
                    // 异步保存
                    cacheService
                            .saveInRedisAsync(new String[]{cachedAnnotation.cacheName()}, cacheKey,
                                    returnObject, cachedAnnotation.ttl());
                } else {
                    // 同步保存
                    cacheService
                            .save(new String[]{cachedAnnotation.cacheName()}, cacheKey,
                                    returnObject, cachedAnnotation.ttl());
                }
            } catch (Exception e) {
                log.error("[缓存] - [Redis缓存数据] - 异常 ：" + e.getMessage(), e);
            }
        }
        return returnObject;
    }

    /**
     * 更新缓存
     *
     * @param joinPoint    切点
     * @param returnObject 返回值
     */
    @AfterReturning(pointcut = "executionOfCachePutMethod()", returning = "returnObject")
    public void putInCache(final JoinPoint joinPoint, final Object returnObject) {
        try {
            if (returnObject == null) {
                return;
            }

            if (!cacheConfigProperties.isEnable()) {
                return;
            }
            CachePut cachePutAnnotation = getAnnotation(joinPoint, CachePut.class);
            Object cacheKey = parseAndGetCacheKeyFromExpression(generateOriginalCachePutKey(joinPoint, cachePutAnnotation));
            if (cachePutAnnotation.isAsync()) {
                cacheService.saveInRedisAsync(cachePutAnnotation.cacheNames(), cacheKey, returnObject, cachePutAnnotation.ttl());
            } else {
                cacheService.save(cachePutAnnotation.cacheNames(), cacheKey, returnObject, cachePutAnnotation.ttl());
            }
        } catch (Exception e) {
            log.error("[缓存] - [缓存更新失败] - " + e.getMessage(), e);
        }
    }


    private Object callActualMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return proceedingJoinPoint.proceed();
    }

    /**
     * 删除删除
     *
     * @param joinPoint    切点
     * @param returnObject 返回值
     */
    @AfterReturning(pointcut = "executionOfCacheDeleteMethod()", returning = "returnObject")
    public void deleteCache(final JoinPoint joinPoint, final Object returnObject) {
        try {
            // 如果没有开启缓存，直接执行方法
            if (!cacheConfigProperties.isEnable()) {
                return;
            }
            // 获取注解
            CacheDelete cacheDeleteAnnotation = getAnnotation(joinPoint, CacheDelete.class);
            // 获取需要操作的缓存名称
            String[] cacheNames = cacheDeleteAnnotation.cacheNames();
            // 获取缓存key
            Object cacheKey = null;
            // 非删除所有缓存
            if (!cacheDeleteAnnotation.removeAll()) {
                cacheKey = parseAndGetCacheKeyFromExpression(generateOriginalCacheDeleteKey(joinPoint, cacheDeleteAnnotation));
            }
            if (cacheDeleteAnnotation.isAsync()) {
                // 异步删除
                if (cacheDeleteAnnotation.removeAll()) {
                    cacheService.invalidateCache(cacheNames);
                } else {
                    cacheService.invalidateCache(cacheNames, cacheKey);
                }
            } else {
                // 同步删除
                if (cacheDeleteAnnotation.removeAll()) {
                    cacheService.invalidateCache(cacheNames);
                } else {
                    cacheService.invalidateCache(cacheNames, cacheKey);
                }
            }
        } catch (Exception e) {
            log.error("[缓存] - [缓存移除失败] - " + e.getMessage(), e);
        }
    }

    /**
     * 获取注解实例
     *
     * @param joinPoint       切点
     * @param annotationClass 注解类
     * @param <T>             注解泛型
     * @return 注解实例
     * @throws NoSuchMethodException 异常
     */
    private <T extends Annotation> T getAnnotation(JoinPoint joinPoint,
                                                   Class<T> annotationClass) throws NoSuchMethodException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();
        if (method.getDeclaringClass().isInterface()) {
            method = joinPoint.getTarget().getClass().getDeclaredMethod(methodName,
                    method.getParameterTypes());
        }
        return method.getAnnotation(annotationClass);
    }

    /**
     * 生成缓存key
     */
    private String generateOriginalCachedKey(JoinPoint joinPoint, Cached cacheOptionAnnotation) {
        // 获取注解
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        if (ObjectUtils.isEmpty(cacheOptionAnnotation)) {
            Asserts.fail("缓存注解解析为空");
        }
        // 默认方法限定名+注解排名（可能多个）
        String prefix = null;
        if (cacheOptionAnnotation != null) {
            prefix = StrUtil.isBlank(cacheOptionAnnotation.cacheName()) ? SpElUtils.getMethodKey(method) : cacheOptionAnnotation.cacheName();
        }
        StringBuilder key = new StringBuilder(prefix + ":");
        Map<String, String> stringStringMap = SpElUtils.parseSpEls(method, joinPoint.getArgs(), cacheOptionAnnotation.cacheKay());
        Set<String> strings = stringStringMap.keySet();
        for (String string : strings) {
            key.append(stringStringMap.get(string)).append(":");
        }
        log.info("[缓存] - [查询/保存] - [解析缓存KEY] : {}", key);
        return key.toString();
    }

    private String generateOriginalCachePutKey(JoinPoint joinPoint, CachePut cacheOptionAnnotation) {
        // 获取注解
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        if (ObjectUtils.isEmpty(cacheOptionAnnotation)) {
            Asserts.fail("缓存注解解析为空");
        }
        // 默认方法限定名+注解排名（可能多个）
        String prefix = null;
        if (cacheOptionAnnotation != null) {
            String[] cacheNames = cacheOptionAnnotation.cacheNames();
            if (ArrayUtils.isNotEmpty(cacheNames)) {
                prefix = SpElUtils.getMethodKey(method);
            } else {
                for (String cacheName : cacheNames) {
                    prefix = cacheName + ":";
                }
            }
        }
        StringBuilder key = new StringBuilder(prefix + ":");
        Map<String, String> stringStringMap = SpElUtils.parseSpEls(method, joinPoint.getArgs(), cacheOptionAnnotation.cacheKey());
        Set<String> strings = stringStringMap.keySet();
        for (String string : strings) {
            key.append(stringStringMap.get(string)).append(":");
        }
        log.info("[缓存] - [更新] - [解析缓存KEY] : {}", key);
        return key.toString();
    }

    private String generateOriginalCacheDeleteKey(JoinPoint joinPoint, CacheDelete cacheOptionAnnotation) {
        // 获取注解
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        if (ObjectUtils.isEmpty(cacheOptionAnnotation)) {
            Asserts.fail("缓存注解解析为空");
        }
        // 默认方法限定名+注解排名（可能多个）
        String prefix = null;
        if (cacheOptionAnnotation != null) {
            String[] cacheNames = cacheOptionAnnotation.cacheNames();
            if (ArrayUtils.isNotEmpty(cacheNames)) {
                prefix = SpElUtils.getMethodKey(method);
            } else {
                for (String cacheName : cacheNames) {
                    prefix = cacheName + ":";
                }
            }
        }
        StringBuilder key = new StringBuilder(prefix + ":");
        Map<String, String> stringStringMap = SpElUtils.parseSpEls(method, joinPoint.getArgs(), cacheOptionAnnotation.cacheKey());
        Set<String> strings = stringStringMap.keySet();
        for (String string : strings) {
            key.append(stringStringMap.get(string)).append(":");
        }
        log.info("[缓存] - [移除] - [解析缓存KEY] : {}", key);
        return key.toString();
    }

    /**
     * 加密缓存key
     *
     * @param originalKey 原始key
     * @return 加密后的key
     */
    public Object parseAndGetCacheKeyFromExpression(String originalKey) {
        return DigestUtils.sha1Hex(originalKey).hashCode();
    }
}
