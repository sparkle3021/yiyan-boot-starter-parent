package com.oho.rocketmq.core.model;

import com.oho.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQListener;

/**
 * RockerMQ 消费者
 *
 * @author MENGJIAO
 */
@Slf4j
public abstract class RocketMQConsumer<T> implements RocketMQListener<T> {
    @Override
    public void onMessage(T t) {
        log.info("ROCKET Consumer 收到消息 ： [ {} ]", JsonUtils.toJson(t));
    }
}
