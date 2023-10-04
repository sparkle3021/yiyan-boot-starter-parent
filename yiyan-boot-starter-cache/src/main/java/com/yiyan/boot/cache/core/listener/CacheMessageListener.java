package com.yiyan.boot.cache.core.listener;


import com.yiyan.boot.cache.autoconfigure.properties.MultiLayerCacheProperties;
import com.yiyan.boot.cache.core.model.CacheMessage;
import com.yiyan.boot.cache.core.service.impl.CaffeineCacheServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.listener.MessageListener;

/**
 * 缓存消息发布/订阅监听器
 */
@Slf4j
public class CacheMessageListener implements MessageListener<CacheMessage> {

    /**
     * Caffeine 缓存管理实现接口
     */
    private final CaffeineCacheServiceImpl caffeineCacheService;

    public CacheMessageListener(CaffeineCacheServiceImpl caffeineCacheService) {
        this.caffeineCacheService = caffeineCacheService;
    }

    /**
     * 监听Redis订阅缓存变化消息
     *
     * @param channel
     * @param cacheMessage
     */
    @Override
    public void onMessage(CharSequence channel, CacheMessage cacheMessage) {
        try {
            log.info("[缓存] - [接收缓存更新通知] - 接收到的消息 ：{}", channel);
            // 如果是本机消息， 不做清除
            if (!MultiLayerCacheProperties.SYSTEM_ID.equals(cacheMessage.getSystemId())) {
                // 清理本地缓存信息
                caffeineCacheService.clearNotSend(cacheMessage.getCacheNames(), cacheMessage.getKey());
                log.info("[缓存] - [接收缓存更新通知] - 清除本地缓存：{}, 缓存Key为: {}",
                        cacheMessage.getCacheNames(), cacheMessage.getKey());
            }
        } catch (Exception e) {
            log.error("[缓存] - [接收缓存更新通知] - 异常 ：" + e.getMessage(), e);
        }
    }
}
