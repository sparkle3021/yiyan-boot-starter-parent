package com.yiyan.boot.cache.core.service.impl;

import com.esotericsoftware.minlog.Log;
import com.yiyan.boot.cache.core.service.RedisSendService;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

/**
 * @author MENGJIAO
 * @createDate 2023-09-28 下午 03:25
 */
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
        // TODO: 对象替换成消息对象
        long receiveCount = rTopic.publish(new Object());
        Log.info("Redisson消息发送成功，接收者数量：" + receiveCount);
    }
}
