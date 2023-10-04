package com.yiyan.boot.cache.core.service.impl;

import com.yiyan.boot.cache.core.model.CacheMessage;
import com.yiyan.boot.cache.core.service.RedisSendService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

/**
 * Redis缓存变更消息发送接口实现类
 *
 * @author MENGJIAO
 * @createDate 2023-09-28 下午 03:25
 */
@Slf4j
public class RedisSendServiceImpl implements RedisSendService {

    private final RedissonClient redissonClient;

    private final String topicName;

    public RedisSendServiceImpl(RedissonClient redissonClient, String topicName) {
        this.redissonClient = redissonClient;
        this.topicName = topicName;
    }

    @Override
    public void sendMessage(String[] cacheNames) {
        sendMessage(cacheNames, null);
    }

    @Override
    public void sendMessage(String cacheName) {
        sendMessage(new String[]{cacheName}, null);
    }

    @Override
    public void sendMessage(String cacheName, Object key) {
        sendMessage(new String[]{cacheName}, key);
    }

    @Override
    public void sendMessage(String[] cacheNames, Object key) {
        RTopic rTopic = redissonClient.getTopic(topicName);
        long receiveCount = rTopic.publish(new CacheMessage(cacheNames, key));
        log.info("[缓存] - [缓存更新通知发送] - 接收者数量：{}", receiveCount);
    }
}
