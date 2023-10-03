package com.yiyan.boot.redis.core.aspect;

import cn.hutool.core.util.StrUtil;
import com.yiyan.boot.common.utils.SpElUtils;
import com.yiyan.boot.redis.core.annotation.RedissonLock;
import com.yiyan.boot.redis.core.utils.RedisLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Redisson 分布式锁切面
 *
 * @author MENGJIAO
 * @createDate 2023-09-18 下午 07:17
 */
@Slf4j
@Aspect
@Component
// 确保比事务注解先执行，分布式锁在事务外
//@Order(0)
public class RedissonLockAspect {

    @Autowired
    private RedisLockUtil redisLockUtil;

    @Pointcut("@annotation(com.yiyan.boot.redis.core.annotation.RedissonLock)")
    public void lockPointcut() {
    }

    @Around("@annotation(com.yiyan.boot.redis.core.annotation.RedissonLock)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        RedissonLock redissonLock = method.getAnnotation(RedissonLock.class);
        // 默认方法限定名+注解排名（可能多个）
        String prefix = StrUtil.isBlank(redissonLock.prefixKey()) ? SpElUtils.getMethodKey(method) : redissonLock.prefixKey();
        String key = prefix + ":" + SpElUtils.parseSpEl(method, joinPoint.getArgs(), redissonLock.key());
        int waitTime = redissonLock.waitTime();
        TimeUnit timeUnit = redissonLock.unit();
        return redisLockUtil.executeWithLockThrows(key, waitTime, timeUnit, joinPoint::proceed);
    }
}
