package com.yiyan.boot.cache.core.service;

/**
 * @author MENGJIAO
 * @createDate 2023-09-28 下午 03:25
 */
public interface RedisSendService {
    /**
     * 发送缓存变更消息
     *
     * @param cacheNames
     */
    void sendMessage(String[] cacheNames);

    /**
     * 发送缓存变更消息
     *
     * @param cacheName
     */
    void sendMessage(String cacheName);

    /**
     * 发送缓存变更消息
     *
     * @param cacheName
     * @param key
     */
    void sendMessage(String cacheName, Object key);

    /**
     * 发送缓存变更消息
     *
     * @param cacheNames
     * @param key
     */
    void sendMessage(String[] cacheNames, Object key);
}
