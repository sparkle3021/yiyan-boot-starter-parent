package com.yiyan.boot.common.utils.cache;

import com.yiyan.boot.common.constant.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.concurrent.TimeUnit;

/**
 * @author MENGJIAO
 * @createDate 2023-11-19 0019 上午 03:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaffeineCacheConfig {
    /**
     * 缓存名称
     */
    @NotBlank(message = "缓存名称不能为空")
    private String cacheName;
    /**
     * 初始的缓存空间大小, 默认为100
     */
    private Integer initialCapacity;
    /**
     * 缓存的最大条数,默认为2000,-1不设置maximumSize
     */
    private Integer maximumSize;
    /**
     * 过期时间 默认为2小时
     */
    private Integer duration;
    /**
     * 时间单位 默认为秒
     */
    private TimeUnit timeUnit;

    public CaffeineCacheConfig(String cacheName) {
        this.cacheName = cacheName;
    }

    public Integer getInitialCapacity() {
        return initialCapacity == null ? Constants.CAFFEINE_DEFAULT_INITIAL_CAPACITY : initialCapacity;
    }

    public Integer getMaximumSize() {
        return maximumSize == null ? Constants.CAFFEINE_DEFAULT_MAXIMUM_SIZE : maximumSize;
    }

    public Integer getDuration() {
        return duration == null ? Constants.CAFFEINE_DEFAULT_EXPIRE_TIME : duration;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit == null ? Constants.CAFFEINE_DEFAULT_TIME_UNIT : timeUnit;
    }
}
