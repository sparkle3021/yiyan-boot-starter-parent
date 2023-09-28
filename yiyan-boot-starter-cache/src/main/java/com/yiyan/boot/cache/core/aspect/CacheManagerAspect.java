package com.yiyan.boot.cache.core.aspect;


import cn.hutool.core.util.StrUtil;
import com.yiyan.boot.cache.autoconfigure.properties.MultiLayerCacheProperties;
import com.yiyan.boot.cache.core.annotation.CacheDelete;
import com.yiyan.boot.cache.core.annotation.CachePut;
import com.yiyan.boot.cache.core.annotation.Cached;
import com.yiyan.boot.cache.core.service.CacheService;
import com.yiyan.boot.cache.core.utils.CacheUtil;
import com.yiyan.boot.common.exception.Asserts;
import com.yiyan.boot.common.utils.ObjectUtils;
import com.yiyan.boot.common.utils.SpElUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * AOP切面，缓存拦截处理
 */
@Aspect
@Order(101)
@Slf4j
@Component
@ConditionalOnProperty(name = "multi.cache.enable", havingValue = "true")
public class CacheManagerAspect {

    @Autowired
    CacheService redisCacheService;

    @Autowired
    private MultiLayerCacheProperties cacheConfigProperties;

    @Pointcut("execution(* com.yiyan..*.*(..)) && @annotation(com.yiyan.boot.cache.core.annotation.Cached)")
    public void executionOfCachedMethod() {
    }

    @Pointcut("execution(* com.yiyan..*.*(..)) && @annotation(com.yiyan.boot.cache.core.annotation.CachePut)")
    public void executionOfCachePutMethod() {
    }

    @Pointcut("execution(* com.yiyan..*.*(..))  && @annotation(com.yiyan.boot.cache.core.annotation.CacheDelete)")
    public void executionOfCacheDeleteMethod() {
    }

    /**
     * 获取缓存，如果没有则执行方法，并将结果缓存
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
            if (StringUtils.isEmpty(cachedAnnotation.keyExpression())) {
                cacheKey = CacheUtil.buildCacheKey(proceedingJoinPoint.getArgs());
            } else {
                cacheKey = parseAndGetCacheKeyFromExpression(generateOriginalCachedKey(proceedingJoinPoint, cachedAnnotation));
            }
            // 从缓存中获取数据
            returnObject = redisCacheService.getFromCache(cachedAnnotation.cacheName(), cacheKey);
        } catch (Exception e) {
            log.error("getAndSaveInCache # Redis op Exception while trying to get from cache ## " + e.getMessage(), e);
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
                    redisCacheService
                            .saveInRedisAsync(new String[]{cachedAnnotation.cacheName()}, cacheKey,
                                    returnObject, cachedAnnotation.TTL());
                } else {
                    // 同步保存
                    redisCacheService
                            .save(new String[]{cachedAnnotation.cacheName()}, cacheKey,
                                    returnObject, cachedAnnotation.TTL());
                }
            } catch (Exception e) {
                log.error("getAndSaveInCache # Exception occurred while trying to save data in redis##" + e.getMessage(),
                        e);
            }
        }
        return returnObject;
    }

    /**
     * 缓存更新
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
                redisCacheService.saveInRedisAsync(cachePutAnnotation.cacheNames(), cacheKey, returnObject, cachePutAnnotation.TTL());
            } else {
                redisCacheService.save(cachePutAnnotation.cacheNames(), cacheKey, returnObject, cachePutAnnotation.TTL());
            }
        } catch (Exception e) {
            log.error("putInCache # Data save failed ## " + e.getMessage(), e);
        }
    }

    /**
     * 缓存删除
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
                    redisCacheService.invalidateCache(cacheNames);
                } else {
                    redisCacheService.invalidateCache(cacheNames, cacheKey);
                }
            } else {
                // 同步删除
                if (cacheDeleteAnnotation.removeAll()) {
                    redisCacheService.invalidateCache(cacheNames);
                } else {
                    redisCacheService.invalidateCache(cacheNames, cacheKey);
                }
            }
        } catch (Exception e) {
            log.error("putInCache # Data delete failed! ## " + e.getMessage(), e);
        }
    }


    private Object callActualMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return proceedingJoinPoint.proceed();
    }

    private <T extends Annotation> T getAnnotation(JoinPoint proceedingJoinPoint,
                                                   Class<T> annotationClass) throws NoSuchMethodException {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();
        if (method.getDeclaringClass().isInterface()) {
            method = proceedingJoinPoint.getTarget().getClass().getDeclaredMethod(methodName,
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
        Map<String, String> stringStringMap = SpElUtils.parseSpEls(method, joinPoint.getArgs(), cacheOptionAnnotation.keyExpression());
        Set<String> strings = stringStringMap.keySet();
        for (String string : strings) {
            key.append(stringStringMap.get(string)).append(":");
        }
        log.info("getAndSaveInCache # key:{}", key);
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
        Map<String, String> stringStringMap = SpElUtils.parseSpEls(method, joinPoint.getArgs(), cacheOptionAnnotation.keyExpression());
        Set<String> strings = stringStringMap.keySet();
        for (String string : strings) {
            key.append(stringStringMap.get(string)).append(":");
        }
        log.info("getAndSaveInCache # key:{}", key);
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
        Map<String, String> stringStringMap = SpElUtils.parseSpEls(method, joinPoint.getArgs(), cacheOptionAnnotation.keyExpression());
        Set<String> strings = stringStringMap.keySet();
        for (String string : strings) {
            key.append(stringStringMap.get(string)).append(":");
        }
        log.info("getAndSaveInCache # key:{}", key);
        return key.toString();
    }

    public Object parseAndGetCacheKeyFromExpression(String originalKey) {
        return DigestUtils.sha1Hex(originalKey).hashCode();
    }

}
