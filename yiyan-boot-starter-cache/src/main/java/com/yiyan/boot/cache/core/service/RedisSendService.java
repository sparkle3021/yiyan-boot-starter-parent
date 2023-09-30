package com.yiyan.boot.cache.core.service;

/**
 * Redis缓存变更消息发送接口
 *
 * @author MENGJIAO
 * @createDate 2023-09-28 下午 03:25
 */
public interface RedisSendService {
    /**
     * 发送缓存变更消息
     *
     * @param cacheNames 缓存名称
     */
    void sendMessage(String[] cacheNames);

    /**
     * 发送缓存变更消息
     *
     * @param cacheName 缓存名称
     */
    void sendMessage(String cacheName);

    /**
     * 发送缓存变更消息
     *
     * @param cacheName 缓存名称
     * @param key       缓存KEY
     */
    void sendMessage(String cacheName, Object key);

    /**
     * 发送缓存变更消息
     *
     * @param cacheNames 缓存名称
     * @param key        缓存KEY
     */
    void sendMessage(String[] cacheNames, Object key);
}
