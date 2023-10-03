# YiYan Redis Starter

## 介绍

基于Redisson实现的Redis工具类

## 目的

1. 提供Redis 基础操作工具， 例如： `set`, `get`, `del`, `incr`, `decr`等。
2. 提供Redis分布式锁工具， 提供不同的枷锁方式以适用不同的场景。
3. 提供基于Redis的分布式Id工具。

## 使用

### 分布式锁的使用

1. 引入依赖

```xml

<dependency>
    <groupId>com.yiyan</groupId>
    <artifactId>yiyan-boot-starter-redis</artifactId>
    <version>0.0.1</version>
</dependency>
```

2. 配置

```yaml
spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password:
    database: 0
  redisson:
    config:
      # 单机模式
      singleServerConfig:
        address: redis://127.0.0.1:6379
```

3. 使用 `Redis分布式锁`

`API 式使用`

```java
import com.yiyan.boot.redis.core.utils.RedisLockUtil;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
public class TestClass {

    @Autowired
    private RedisLockUtil redisLockUtil;

    private static final String LOCK_KET = "redisson:lock:xxx";

    /**
     * 不可重入锁
     */
    public void redisLockApi1() {
        RLock lock = redisLockUtil.getLock(LOCK_KET);
        try {
            lock.lock();
            // do something
        } finally {
            lock.unlock();
        }
    }

    /**
     * 可重入锁
     */
    public void redisLockApi2() {
        RLock lock = redisLockUtil.getLock(LOCK_KET);
        try {
            if (lock.tryLock()) {
                // do something
            }
        } finally {
            lock.unlock();
        }
    }
}
```

`注解式使用`

```java
import com.yiyan.boot.redis.core.annotation.RedissonLock;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TestClass {

    /**
     * Redis 分布式锁注解
     */
    @RedissonLock(prefixKey = "redis:lock:test", key = "#id", waitTime = 1000, unit = TimeUnit.SECONDS)
    public void redisLockAnnotation(String id) {
        // do something
    }
}
```

> 注解参数：
> 1. `prefixKey`： 锁前缀， 默认值： 方法名
> 2. `key`： 锁key， 无默认值， 必填
> 3. `waitTime`： 等待时间， 默认值： 0
> 4. `unit` ： 时间单位， 默认值： TimeUnit.SECONDS


> 区别：
> 1. `API 式使用`： 可不同的业务场景锁颗粒度要求，根据需求给代码块上锁。`注解式使用`： 适用于对锁的颗粒度要求不高的业务场景，
     只需要在方法上添加注解即可。
> 2. `API 式使用`： 可以根据业务场景选择不同的加锁方式。`注解式使用`： 只能使用默认的加锁方式。

### 分布式Id的使用
```java
import com.yiyan.boot.redis.core.utils.RedisDistributedId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestClass {

    @Autowired
    private RedisDistributedId redisDistributedId;

    private static final String ID_KEY = "redis:key:test";

    /**
     * 根据KEY获取下一个ID，基于时间
     */
    public void redisDistributedId1() {
        long id = redisDistributedId.nextId(ID_KEY);
        System.out.println(id);
    }

    /**
     * 根据KEY和号码段获取下一个ID
     */
    public void redisDistributedId2() {
        long id = redisDistributedId.nextId(ID_KEY, 1000);
        System.out.println(id);
    }

    /**
     * 根据Redis incr获取下一个ID
     */
    public void redisDistributedId3() {
        long id = redisDistributedId.nextIdByIncrement(ID_KEY);
        System.out.println(id);
    }

}
```