package com.oho.redis.autoconfigure.properties;

import io.netty.channel.EventLoopGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.redisson.api.NameMapper;
import org.redisson.client.DefaultNettyHook;
import org.redisson.client.NettyHook;
import org.redisson.client.codec.Codec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.SslProvider;
import org.redisson.config.TransportMode;
import org.redisson.connection.AddressResolverGroupFactory;
import org.redisson.connection.ConnectionListener;
import org.redisson.connection.SequentialDnsAddressResolverFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Sparkler
 * @createDate 2023/2/16-9:29
 */
@Data
@ConfigurationProperties(prefix = "spring.redisson")
@Configuration
public class RedissonProperties {

    /**
     * 是否启用Redisson。默认禁用
     */
    private Boolean enable = false;

    /**
     * threads（线程池数量）
     * 默认值: `当前处理核数量 * 2`
     * 这个线程池数量被所有`RTopic`对象监听器，`RRemoteService`调用者和`RExecutorService`任务共同共享。
     */
    private int threads = 16;


    /**
     * nettyThreads （Netty线程池数量）
     * 默认值: `当前处理核数量 * 2`
     * 这个线程池数量是在一个Redisson实例内，被其创建的所有分布式数据类型和服务，以及底层客户端所一同共享的线程池里保存的线程数量。
     */
    private int nettyThreads = 32;

    /**
     * codec（编码）
     * 默认值: `org.redisson.codec.JsonJacksonCodec`
     * Redisson的对象编码类是用于将对象进行序列化和反序列化，以实现对该对象在Redis里的读取和存储。
     */
    private Codec codec = new JsonJacksonCodec();

    /**
     * executor（线程池）
     * 单独提供一个用来执行所有`RTopic`对象监听器，`RRemoteService`调用者和`RExecutorService`任务的线程池（ExecutorService）实例。
     */
    private ExecutorService executor;

    /**
     * transportMode（传输模式）
     * 默认值：`TransportMode.NIO`
     * 可选参数：
     * `TransportMode.NIO`,
     * `TransportMode.EPOLL` - 需要依赖里有`netty-transport-native-epoll`包（Linux）
     * `TransportMode.KQUEUE` - 需要依赖里有 `netty-transport-native-kqueue`包（macOS）
     */
    private TransportMode transportMode = TransportMode.NIO;

    /**
     * eventLoopGroup
     * 用于特别指定一个EventLoopGroup. EventLoopGroup是用来处理所有通过Netty与Redis服务之间的连接发送和接受的消息。每一个Redisson都会在默认情况下自己创建管理一个EventLoopGroup实例。因此，如果在同一个JVM里面可能存在多个Redisson实例的情况下，采取这个配置实现多个Redisson实例共享一个EventLoopGroup的目的。
     * 只有`io.netty.channel.epoll.EpollEventLoopGroup`或`io.netty.channel.nio.NioEventLoopGroup`才是允许的类型。
     */
    private EventLoopGroup eventLoopGroup;

    /**
     * lockWatchdogTimeout（监控锁的看门狗超时，单位：毫秒）
     * <p>
     * 默认值：`30000`
     * <p>
     * 监控锁的看门狗超时时间单位为毫秒。该参数只适用于分布式锁的加锁请求中未明确使用`leaseTimeout`参数的情况。
     * 如果该看门口未使用<code>lockWatchdogTimeout</code>去重新调整一个分布式锁的<code>lockWatchdogTimeout</code>超时，那么这个锁将变为失效状态。这个参数可以用来避免由Redisson客户端节点宕机或其他原因造成死锁的情况。
     */
    private long lockWatchdogTimeout = 30 * 1000;

    /**
     * keepPubSubOrder（保持订阅发布顺序）
     * 默认值：`true`
     * 通过该参数来修改是否按订阅发布消息的接收顺序出来消息，如果选否将对消息实行并行处理，该参数只适用于订阅发布消息的情况。
     */
    private boolean keepPubSubOrder = true;

    private boolean checkLockSyncedSlaves = true;

    private long reliableTopicWatchdogTimeout = TimeUnit.MINUTES.toMillis(10);

    private boolean referenceEnabled = true;

    private boolean useScriptCache = false;

    private int minCleanUpDelay = 5;

    private int maxCleanUpDelay = 30 * 60;

    private int cleanUpKeysAmount = 100;

    private NettyHook nettyHook = new DefaultNettyHook();

    private ConnectionListener connectionListener;

    private boolean useThreadClassLoader = true;

    private AddressResolverGroupFactory addressResolverGroupFactory = new SequentialDnsAddressResolverFactory();

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    public static class ClusterServersConfig extends org.redisson.config.ClusterServersConfig {
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    public static class ReplicatedServersConfig extends org.redisson.config.ReplicatedServersConfig {
    }

    @Data
    public static class SingleServerConfig {
        /**
         * address（节点地址）
         * <p>
         * 可以通过`host:port`的格式来指定节点地址。
         */
        private String address = "redis://127.0.0.1:6379";

        /**
         * subscriptionConnectionMinimumIdleSize（发布和订阅连接的最小空闲连接数）
         * <p>
         * 默认值：`1`
         * <p>
         * 用于发布和订阅连接的最小保持连接数（长连接）。Redisson内部经常通过发布和订阅来实现许多功能。长期保持一定数量的发布订阅连接是必须的。
         */
        private Integer subscriptionConnectionMinimumIdleSize = 1;
        /**
         * idleConnectionTimeout（连接空闲超时，单位：毫秒）
         * <p>
         * 默认值：`10000`
         * <p>
         * 如果当前连接池里的连接数量超过了最小空闲连接数，而同时有连接空闲时间超过了该数值，那么这些连接将会自动被关闭，并从连接池里去掉。时间单位是毫秒。
         */

        private Integer idleConnectionTimeout = 10000;
        /**
         * connectTimeout（连接超时，单位：毫秒）
         * <p>
         * 默认值：`10000`
         * <p>
         * 同节点建立连接时的等待超时。时间单位是毫秒。
         */
        private Integer connectTimeout = 10000;
        /**
         * timeout（命令等待超时，单位：毫秒）
         * <p>
         * 默认值：`3000`
         * <p>
         * 等待节点回复命令的时间。该时间从命令发送成功时开始计时。
         */
        private Integer timeout = 3000;
        /**
         * retryAttempts（命令失败重试次数）
         * <p>
         * 默认值：`3`
         * <p>
         * 如果尝试达到 **retryAttempts（命令失败重试次数）** 仍然不能将命令发送至某个指定的节点时，将抛出错误。如果尝试在此限制之内发送成功，则开始启用 **timeout（命令等待超时）** 计时。
         */
        private Integer retryAttempts = 3;
        /**
         * retryInterval（命令重试发送时间间隔，单位：毫秒）
         * <p>
         * 默认值：`1500`
         * <p>
         * 在某个节点执行相同或不同命令时，**连续** 失败 **failedAttempts（执行失败最大次数）** 时，该节点将被从可用节点列表里清除，直到 **reconnectionTimeout（重新连接时间间隔）** 超时以后再次尝试。
         */
        private Integer retryInterval = 1500;
        /**
         * database（数据库编号）
         * <p>
         * 默认值：`0`
         * <p>
         * 尝试连接的数据库编号。
         */
        private Integer database = 0;

        private String username = null;
        /**
         * password（密码）
         * <p>
         * 默认值：`null`
         * <p>
         * 用于节点身份验证的密码。
         */
        private String password = null;
        /**
         * subscriptionsPerConnection（单个连接最大订阅数量）
         * <p>
         * 默认值：`5`
         * <p>
         * 每个连接的最大订阅数量。
         */
        private Integer subscriptionsPerConnection = 5;
        /**
         * clientName（客户端名称）
         * <p>
         * 默认值：`null`
         * <p>
         * 在Redis节点里显示的客户端名称。
         */
        private String clientName = null;

        /**
         * subscriptionConnectionPoolSize（发布和订阅连接池大小）
         * <p>
         * 默认值：`50`
         * <p>
         * 用于发布和订阅连接的连接池最大容量。连接池的连接数量自动弹性伸缩。
         */

        private Integer subscriptionConnectionPoolSize = 50;
        /**
         * connectionMinimumIdleSize（最小空闲连接数）
         * <p>
         * 默认值：`32`
         * <p>
         * 最小保持连接数（长连接）。长期保持一定数量的连接有利于提高瞬时写入反应速度。
         */
        private Integer connectionMinimumIdleSize = 32;
        /**
         * connectionPoolSize（连接池大小）
         * <p>
         * 默认值：`64`
         * <p>
         * 在启用该功能以后，Redisson将会监测DNS的变化情况。
         */
        private Integer connectionPoolSize = 64;
        /**
         * dnsMonitoringInterval（DNS监测时间间隔，单位：毫秒）
         * <p>
         * 默认值：`5000`
         * <p>
         * 监测DNS的变化情况的时间间隔。
         */
        private Integer dnsMonitoringInterval = 5000;

        /**
         * sslEnableEndpointIdentification（启用SSL终端识别）
         * <p>
         * 默认值：`true`
         * <p>
         * 开启SSL终端识别能力。
         */
        private Boolean sslEnableEndpointIdentification = true;
        /**
         * sslProvider（SSL实现方式）
         * <p>
         * 默认值：`JDK`
         * <p>
         * 确定采用哪种方式（JDK或OPENSSL）来实现SSL连接。
         */
        private SslProvider sslProvider = SslProvider.JDK;
        /**
         * sslTruststore（SSL信任证书库路径）
         * <p>
         * 默认值：`null`
         * <p>
         * 指定SSL信任证书库的路径。
         */
        private URL sslTruststore = null;
        /**
         * sslTruststorePassword（SSL信任证书库密码）
         * <p>
         * 默认值：`null`
         * <p>
         * 指定SSL信任证书库的密码。
         */
        private String sslTruststorePassword = null;
        /**
         * sslKeystore（SSL钥匙库路径）
         * <p>
         * 默认值：`null`
         * <p>
         * 指定SSL钥匙库的路径。
         */
        private URL sslKeystore = null;
        /**
         * sslKeystorePassword（SSL钥匙库密码）
         * <p>
         * 默认值：`null`
         * <p>
         * 指定SSL钥匙库的密码。
         */
        private String sslKeystorePassword = null;

        private int pingConnectionInterval = 3000;

        private Boolean keepAlive = false;

        private Boolean tcpNoDelay = true;

        private String[] sslProtocols = null;

        private NameMapper nameMapper = null;
    }


    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class SentinelServersConfig extends org.redisson.config.SentinelServersConfig {
        public SentinelServersConfig() {
            super();
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class MasterSlaveServersConfig extends org.redisson.config.MasterSlaveServersConfig {
        public MasterSlaveServersConfig() {
            super();
        }
    }

    /**
     * 集群模式
     */
    private ClusterServersConfig clusterServersConfig;

    /**
     * 云托管模式
     */
    private ReplicatedServersConfig replicatedServersConfig;

    /**
     * 单Redis节点模式
     */
    private SingleServerConfig singleServerConfig;

    /**
     * 哨兵模式
     */
    private SentinelServersConfig sentinelServersConfig;

    /**
     * 主从模式
     */
    private MasterSlaveServersConfig masterSlaveServersConfig;

}
