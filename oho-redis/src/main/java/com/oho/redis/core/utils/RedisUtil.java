package com.oho.redis.core.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @author MENGJIAO
 * @createDate 2023-05-08 9:38
 */
@Component
public class RedisUtil {

    @Resource(name = "comRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    // ============================= String类型操作 ============================

    /**
     * 将值存储到Redis中
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 将值存储到Redis中
     *
     * @param key      键
     * @param value    值
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     */
    public void set(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 根据键获取Redis中的值
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // ============================= Hash类型操作 ============================

    /**
     * 将值存储到Redis中
     *
     * @param key     键
     * @param hashKey hash键
     * @param value   值
     */
    public void hset(String key, Object hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 将值存储到Redis中
     *
     * @param key      键
     * @param hashKey  hash键
     * @param value    值
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     */
    public void hset(String key, Object hashKey, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForHash().put(key, hashKey, value);
        redisTemplate.expire(key, timeout, timeUnit);
    }

    /**
     * 根据键和Hash键获取Redis中的值
     *
     * @param key     键
     * @param hashKey hash键
     * @return 值
     */
    public Object hget(String key, Object hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 根据Key，获取整个Hash类型的数据
     *
     * @param key 键
     * @return 值
     */
    public Map<Object, Object> hgetall(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 根据Key，删除Hash类型的数据
     *
     * @param key      键
     * @param hashKeys hash键
     */
    public void hdel(String key, Object... hashKeys) {
        redisTemplate.opsForHash().delete(key, hashKeys);
    }

    // ============================= List类型操作 ============================

    /**
     * 向List数据类型中添加值
     *
     * @param key   键
     * @param value 值
     */
    public void lpush(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 向List数据类型中添加值
     *
     * @param key      键
     * @param value    值
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     */
    public void lpush(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForList().leftPush(key, value);
        redisTemplate.expire(key, timeout, timeUnit);
    }

    /**
     * 从List数据类型中获取值
     *
     * @param key   键
     * @param start 起始位置
     * @param end   结束位置
     * @return 值
     */
    public List<Object> lrange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 移除集合左侧第一个元素
     *
     * @param key 键
     * @return 值
     */
    public Object lpop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 移除集合右侧第一个元素
     *
     * @param key 键
     * @return 值
     */
    public Object rpop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    // ============================= Set类型操作 ============================

    /**
     * 在Set数据类型中添加值
     *
     * @param key    键
     * @param values 值
     */
    public void sadd(String key, Object... values) {
        redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 在Set数据类型中添加值
     *
     * @param key      键
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     * @param values   值
     */
    public void sadd(String key, long timeout, TimeUnit timeUnit, Object... values) {
        redisTemplate.opsForSet().add(key, values);
        redisTemplate.expire(key, timeout, timeUnit);
    }

    /**
     * 获取Set的所有元素。
     *
     * @param key 键
     * @return 所有值
     */
    public Set<Object> smembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 从Set数据类型中删除值
     *
     * @param key    键
     * @param values 值
     */
    public void srem(String key, Object... values) {
        redisTemplate.opsForSet().remove(key, values);
    }

    // ============================= ZSet类型操作 ============================

    /**
     * 在ZSet数据类型中添加值
     *
     * @param key   键
     * @param value 值
     * @param score 分值
     */
    public void zadd(String key, Object value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 在ZSet数据类型中添加值
     *
     * @param key      键
     * @param value    值
     * @param score    分值
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     */
    public void zadd(String key, Object value, double score, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForZSet().add(key, value, score);
        redisTemplate.expire(key, timeout, timeUnit);
    }

    /**
     * 获取ZSet的范围元素。
     *
     * @param key   键
     * @param start 起始位置
     * @param end   结束位置
     * @return Set类型的值
     */
    public Set<Object> zrange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 删除ZSet数据类型中的值
     *
     * @param key    键
     * @param values 值
     */
    public void zrem(String key, Object... values) {
        redisTemplate.opsForZSet().remove(key, values);
    }

    // ============================= Common ============================

    /**
     * 判断Key是否存在
     *
     * @param key 键
     * @return 存在返回true，否则返回false
     */
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 删除Key
     *
     * @param key 键
     */
    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    /**
     * 设置Key的过期时间
     *
     * @param key      键
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     * @return 设置成功返回true，否则返回false
     */
    public boolean expire(String key, long timeout, TimeUnit timeUnit) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, timeUnit));
    }

    /**
     * 获取Key的过期时间
     *
     * @param key 键
     * @return 过期时间
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 获取Key的过期时间
     *
     * @param key      键
     * @param timeUnit 时间单位
     * @return 过期时间
     */
    public Long getExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 递增操作
     *
     * @param key   键
     * @param delta 增加的值
     * @return 递增后的值，如果键不存在，则返回-1
     */
    public long incr(String key, long delta) {
        Long result = redisTemplate.opsForValue().increment(key, delta);
        return result != null ? result : -1;
    }

    /**
     * 递减操作
     *
     * @param key   键
     * @param delta 减少的值
     * @return 递减后的值，如果键不存在，则返回-1
     */
    public long decr(String key, long delta) {
        Long result = redisTemplate.opsForValue().increment(key, -delta);
        return result != null ? result : -1;
    }

}

