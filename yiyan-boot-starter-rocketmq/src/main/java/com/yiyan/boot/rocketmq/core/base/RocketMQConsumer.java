package com.yiyan.boot.rocketmq.core.base;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQListener;

/**
 * RockerMQ 消费者
 *
 * @author MENGJIAO
 */
@Slf4j
public abstract class RocketMQConsumer<T> implements RocketMQListener<T> {

}
