package com.yiyan.boot.cache.autoconfigure.properties;

import cn.hutool.core.lang.UUID;
import com.yiyan.boot.common.utils.oshi.SystemInfoUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 多级缓存配置
 *
 * @author MENGJIAO
 * @createDate 2023-09-28 下午 01:51
 */
@Data
@Configuration("cacheConfigProperties")
@ConfigurationProperties(prefix = "multi.cache")
public class MultiLayerCacheProperties {

    public static String SYSTEM_ID;

    static {
        // 获取系统唯一标识
        try {
            SYSTEM_ID = SystemInfoUtils.getMacAddress();
        } catch (Exception e) {
            SYSTEM_ID = UUID.randomUUID().toString();
        }
    }

    /**
     * 是否开启多级缓存
     */
    private boolean enable = false;
    /**
     * 是否开启二级缓存
     */
    private boolean secondCacheEnable = false;
    /**
     * 本地缓存配置
     */
    private LocalCache localCache;
    /**
     * 远程缓存配置
     */
    private RemoteCache remoteCache;
    /**
     * 异步线程池配置
     */
    private AsyncExecutor asyncExecutor;

    @Data
    public static class LocalCache {
        /**
         * 初始化容量
         */
        private int initialCapacity = 100;
        /**
         * 最大容量
         */
        private int maximumSize = 1000;
        /**
         * 最后写过后过期时间 单位：秒
         */
        private int expireAfterWrite = 60;
        /**
         * 最后一次读或写操作后经过指定时间过期 单位：秒
         */
        private int expireAfterAccess;
        /**
         * 写入后多久刷新 单位：秒
         */
        private int refreshAfterWrite;

    }

    @Data
    public static class RemoteCache {
        /**
         * Host
         */
        private String host = "localhost";
        /**
         * Port
         */
        private int port = 6379;
        /**
         * 密码
         */
        private String password = null;
        /**
         * 数据库
         */
        private int database = 0;
        /**
         * 最大连接数
         */
        private int maxActive = 8;
        /**
         * 最大空闲连接数
         */
        private int maxIdle = 8;
        /**
         * 最小空闲连接数
         */
        private int minIdle = 0;
        /**
         * 连接池最大连接数
         */
        private int poolMaxSize = 64;
        /**
         * 连接池最小空闲连接数
         */
        private int minIdleSize = 10;
        /**
         * 连接超时时间
         */
        private int timeout = 10000;
        /**
         * 最大等待时间
         */
        private int maxWait = 10000;
        /**
         * 是否集群
         */
        private boolean cluster = false;
        /**
         * 集群节点 格式：ip:port,ip:port
         */
        private String nodes = null;
        /**
         * 集群最大重定向次数
         */
        private int maxRedirects = 3;
        /**
         * 是否开启压缩
         */
        private boolean useCompression = false;
        /**
         * 分布式缓存更新的的topic名称
         */
        private String topicName = "multi-cache:redisson:topic";
    }

    /**
     * 异步线程池配置
     */
    @Data
    public static class AsyncExecutor {
        /**
         * 核心线程数
         */
        private int corePoolSize = 10;
        /**
         * 最大线程数
         */
        private int maxPoolSize = 20;
        /**
         * 队列容量
         */
        private int queueCapacity = 100;
        /**
         * 线程存活时间
         */
        private int keepAliveSeconds = 60;
        /**
         * 线程名称前缀
         */
        private String threadNamePrefix = "cache-async-executor-";
    }
}
