# YiYan Cache Starter

## 介绍
YiYan Cache Starter 是一个基于 Spring Boot 的缓存组件，支持 Redis 和 Caffeine 两种缓存方式。

## 目的
1. 结合本地缓存和分布式缓存，减少网络消耗，提升接口性能。
2. 通过AOP方式拦截处理， 不侵入原业务逻辑。

## 缓存结构设计

接口流程

![接口使用流程.png](/doc/images/接口使用流程.png)

多实例缓存流程

![缓存组件架构设计.png](/doc/images/缓存组件架构设计.png)

## 使用
引入依赖
```xml
<dependency>
    <groupId>com.yiyan</groupId>
    <artifactId>yiyan-boot-starter-cache</artifactId>
    <version>1.0</version>
</dependency>
```
编写配置文件
```yml
multi:
  cache:
    # 是否开启缓存
    enable: true
    # 是否开启二级缓存
    secondCacheEnable: true
    # 本地缓存配置
    localCache:
      # 初始化容量
      initial-capacity: 100
      # 最大容量
      maximum-size: 1000
      # 写入后过期时间
      expireAfterWrite: 60
      # 访问后过期时间
      expire-after-access: 60
      # 刷新后过期时间
      refresh-after-write: 60

    # 如果已存在Redis相关配置，则优先使用项目配置（Spring.redis.xxx）
    # 且如果同时配置了单机与集群，优先使用单机配置
    remoteCache:
      # redis host
      host: localhost
      # redis port
      port: 6379
      # Redis 密码
      password:
      # Redis 数据库索引（默认为 0）
      database: 0
      # Redis 连接超时时间（毫秒）
      timeout: 10000
      # 是否启用集群配置
      cluster: true
      # 集群节点
      nodes: "redis://localhost:6379,redis://localhost:6380,redis://localhost:6381"
    # 异步线程池配置
    asyncExecutor:
      # 核心线程数
      core-pool-size: 10
      # 最大线程数
      max-pool-size: 20
      # 队列容量
      queue-capacity: 100
      # 线程存活时间
      keep-alive-seconds: 60
      # 线程名称前缀
      thread-name-prefix: "Async-Cache-Executor-"
    
```

简单使用
```java
public class CacheTest {

    @Cached(cacheName = "cache_test", cacheKay = "#id", ttl = 60, isAsync = true)
    public String cached(String id) {
        return "cached";
    }

    @CachePut(cacheNames = "cache_test", cacheKey = "#id", ttl = 60, isAsync = true)
    public String cachePut(String id) {
        return "cached";
    }

    @CacheDelete(cacheNames = "cache_test", cacheKey = "id", isAsync = true)
    public String cacheDelete(String id) {
        return "cached";
    }

    /**
     * 请求参数是对象时，如分页请求，需要指定参数名称
     */
    @Cached(cacheName = "cache_test", cacheKay = "#param.id,#param.username", ttl = 60, isAsync = true)
    public String cached(CacheParam param) {
        return "cached";
    }

    public static class CacheParam {

        private String id;

        private String username;
    }
}
```

注解参数
```
@Cached: 保存缓存
    cacheName: 缓存名称
    cacheKey: 缓存key，支持SpEL表达式
    ttl: 缓存过期时间，单位秒
    isAsync: 是否异步执行，默认false

@CachePut: 更新缓存
    cacheNames: 缓存名称
    cacheKey: 缓存key，支持SpEL表达式
    ttl: 缓存过期时间，单位秒
    isAsync: 是否异步执行，默认false

@CacheDelete: 删除缓存
    cacheNames: 缓存名称
    cacheKey: 缓存key，支持SpEL表达式
    isAsync: 是否异步执行，默认false
```