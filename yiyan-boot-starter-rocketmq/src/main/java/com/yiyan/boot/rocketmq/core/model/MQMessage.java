package com.yiyan.boot.rocketmq.core.model;

import com.yiyan.boot.common.utils.IdUtils;
import com.yiyan.boot.common.utils.StringUtils;
import com.yiyan.boot.rocketmq.core.enums.DelayLevel;
import lombok.Data;

import java.util.List;

/**
 * RocketMQ消息体
 *
 * @author MENGJIAO
 */
@Data
public class MQMessage<T> {
    /**
     * 消息Topic
     */
    private String topic;
    /**
     * tag
     */
    private String tag;
    /**
     * 消息目的地
     */
    private String destination;
    /**
     * 消息Id
     */
    private String messageId;
    /**
     * 消息生产时间
     */
    private long generationTime;
    /**
     * 消息来源：非必须
     */
    private String source = "DEFAULT";
    /**
     * 消息内容
     */
    private T content;
    /**
     * 消息内容
     */
    private List<T> contents;
    /**
     * 使用有序消息发送时，指定发送到队列
     */
    private String hashKey;
    /**
     * RocketMQ 消息延迟级别，默认0即不延迟
     */
    private DelayLevel delayLevel = DelayLevel.LEVEL_0;


    public static final String CONNECTOR = ":";

    /**
     * 获取消息目的地
     * 默认使用：destination
     * 如果topic不为空，则使用：topic:tag
     * @return 消息目的地
     */
    public String getDestination() {
        if (StringUtils.isNotBlank(topic)) {
            return topic + (StringUtils.isNotBlank(tag) ? (CONNECTOR + tag) : "");
        }
        return destination;
    }

    public MQMessage(String destination, T content) {
        this.destination = destination;
        this.messageId = IdUtils.getSnowflakeNextIdStr();
        this.generationTime = System.currentTimeMillis();
        this.content = content;
    }

    public MQMessage(String topic, String tag, T content) {
        this.topic = topic;
        this.tag = tag;
        this.messageId = IdUtils.getSnowflakeNextIdStr();
        this.generationTime = System.currentTimeMillis();
        this.content = content;
    }

    public MQMessage(String topic, String tag, String source, T content) {
        this.topic = topic;
        this.tag = tag;
        this.messageId = IdUtils.getSnowflakeNextIdStr();
        this.generationTime = System.currentTimeMillis();
        this.source = source;
        this.content = content;
    }

    public MQMessage(String topic, String tag, String messageId, String source, T content) {
        this.topic = topic;
        this.tag = tag;
        this.messageId = messageId;
        this.generationTime = System.currentTimeMillis();
        this.source = source;
        this.content = content;
    }
}
