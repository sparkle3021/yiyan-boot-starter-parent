package com.yiyan.boot.redis.core.utils;

import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Redis 分布式ID生成器
 *
 * @author MENGJIAO
 * @createDate 2023-05-15 14:37
 */
@Component
public class RedisDistributedId {

    /**
     * Id计算起始时间戳
     */
    private static final long BEGIN_TIMESTAMP = 1659312000L;


    @Resource(name = "redissonClient")
    private RedissonClient redissonClient;

    @Resource(name = "comRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;
    /**
     * 当前号段的最小Id
     */
    private long minId;

    /**
     * 当前号段的最大Id
     */
    private long maxId;

    /**
     * 生成分布式ID
     * 符号位 时间戳[31位] 自增序号【32位】
     *
     * @param item 业务编号
     * @return Id
     */
    public long nextId(String item) {
        // 1.生成时间戳
        LocalDateTime now = LocalDateTime.now();
        // 格林威治时间差
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        // 我们需要获取的 时间戳 信息
        long timestamp = nowSecond - BEGIN_TIMESTAMP;
        // 2.生成序号 --》 从Redis中获取
        // 当前当前的日期
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        // 获取对应的自增的序号
        Long increment = redisTemplate.opsForValue().increment("id:" + item + ":" + date);
        return timestamp << 32 | increment;
    }

    /**
     * 获取下一个ID
     * </p>
     * 基于Redis的号段模式分布式Id
     *
     * @param idSegmentKey 存储Id号段的Redis键名
     * @param segmentSize  Id号段的大小即一个号段可以生成的ID数量
     * @return 下一个ID long
     */
    public synchronized long nextId(String idSegmentKey, long segmentSize) {
        if (minId == maxId) {
            refreshSegment(idSegmentKey, segmentSize);
        }
        return minId++;
    }

    /**
     * 刷新ID段
     */
    private void refreshSegment(String idSegmentKey, long segmentSize) {
        // 从Redis中获取下一个ID段
        String segment = (String) redisTemplate.opsForValue().getAndSet(idSegmentKey, "1:" + segmentSize);
        if (segment == null) {
            // Redis中不存在ID段，需要初始化一个ID段
            segment = "1:" + segmentSize;
            redisTemplate.opsForValue().set(idSegmentKey, segment);
        }
        String[] segments = segment.split(":");
        minId = Long.parseLong(segments[0]) * segmentSize;
        maxId = minId + segmentSize;
    }

    /**
     * 基于Redisson 原子增长的方式生成分布式ID
     */
    public synchronized long nextIdByIncrement(String idSegmentKey) {
        return redissonClient.getAtomicLong(idSegmentKey).incrementAndGet();
    }

    /**
     * 基于Redisson 原子增长的方式生成分布式ID,每天从0开始
     */
    public synchronized long nextIdByIncrementOnDay(String idSegmentKey) {
        // 判断Redis中是否存在该key
        if (!redissonClient.getAtomicLong(idSegmentKey).isExists()) {
            // 不存在，设置为0
            redissonClient.getAtomicLong(idSegmentKey).set(0);
            // 设置过期时间为当天的最后一秒
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime endOfDay = now.withHour(23).withMinute(59).withSecond(59);
            redissonClient.getAtomicLong(idSegmentKey).expireAt(endOfDay.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        }
        return redissonClient.getAtomicLong(idSegmentKey).getAndIncrement();
    }
}