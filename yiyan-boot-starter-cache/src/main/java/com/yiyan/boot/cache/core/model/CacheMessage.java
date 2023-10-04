package com.yiyan.boot.cache.core.model;

import com.yiyan.boot.cache.autoconfigure.properties.MultiLayerCacheProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * 缓存发布/订阅传输消息对象
 *
 * @author MENGJIAO
 * @createDate 2023-09-28 下午 04:06
 */
@Data
public class CacheMessage implements Serializable {

    /**
     * 系统唯一标识
     */
    private String systemId = MultiLayerCacheProperties.SYSTEM_ID;

    /**
     * 缓存名称
     */
    private String[] cacheNames;

    /**
     * 缓存KEY键值
     */
    private Object key;

    public CacheMessage() {
    }

    public CacheMessage(String[] cacheName, Object key) {
        this.cacheNames = cacheName;
        this.key = key;
    }

    public CacheMessage(String cacheName, Object key) {
        this.cacheNames = new String[]{cacheName};
        this.key = key;
    }
}
