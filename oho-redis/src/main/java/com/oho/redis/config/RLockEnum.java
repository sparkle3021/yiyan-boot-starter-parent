package com.oho.redis.config;

/**
 * Redisson 所类型
 *
 * @author Sparkler
 * @createDate 2023/2/16
 */
public enum RLockEnum {
    /**
     * 可重入锁（Reentrant Lock）
     */
    ReentrantLock,
    /**
     * 公平锁（Fair Lock）
     * <p>
     * 基于Redis的Redisson分布式可重入公平锁也是实现了`java.util.concurrent.locks.Lock`接口的一种`RLock`对象。
     * 同时还提供了[异步（Async）]、 [反射式（Reactive）] 和[RxJava2标准]
     * 它保证了当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。
     * 所有请求线程会在一个队列中排队，当某个线程出现宕机时，Redisson会等待5秒后继续下一个线程，也就是说如果前面有5个线程都处于等待状态，
     * 那么后面的线程会等待至少25秒。
     */
    FairLock,
    /**
     * 联锁（MultiLock）
     * 基于Redis的Redisson分布式联锁[`RedissonMultiLock`]对象可以将多个`RLock`对象关联为一个联锁，
     * 每个`RLock`对象实例可以来自于不同的Redisson实例。
     */
    MultiLock,
    /**
     * 红锁（RedLock）
     * 基于Redis的Redisson红锁`RedissonRedLock`对象实现了[Redlock]介绍的加锁算法。
     * 该对象也可以用来将多个`RLock`对象关联为一个红锁，每个`RLock`对象实例可以来自于不同的Redisson实例。
     */
    RedLock,
    /**
     * 读锁
     * 分布式可重入读写锁允许同时有多个读锁和一个写锁处于加锁状态。
     */
    ReadLock,
    /**
     * 写锁
     * 分布式可重入读写锁允许同时有多个读锁和一个写锁处于加锁状态。
     */
    WriteLock,
    /**
     * 信号量
     */
    Semaphore,
    /**
     * 可过期性信号量
     */
    PermitExpirableSemaphore,
    /**
     * 闭锁（CountDownLatch）
     */
    CountDownLatch
}
