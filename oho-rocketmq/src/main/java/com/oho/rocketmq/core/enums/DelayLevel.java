package com.oho.rocketmq.core.enums;

/**
 * RocketMQ 消息延迟级别.
 *
 * @author MENGJIAO
 */
public enum DelayLevel {
    /**
     * 无延迟
     */
    LEVEL_0(0),
    /**
     * 延迟1s
     */
    LEVEL_1(1),
    /**
     * 延迟5s
     */
    LEVEL_2(2),
    /**
     * 延迟10s
     */
    LEVEL_3(3),
    /**
     * 延迟30s
     */
    LEVEL_4(4),
    /**
     * 延迟1m
     */
    LEVEL_5(5),
    /**
     * 延迟2m
     */
    LEVEL_6(6),
    /**
     * 延迟3m
     */
    LEVEL_7(7),
    /**
     * 延迟4m
     */
    LEVEL_8(8),
    /**
     * 延迟5m
     */
    LEVEL_9(9),
    /**
     * 延迟6m
     */
    LEVEL_10(10),
    /**
     * 延迟7m
     */
    LEVEL_11(11),
    /**
     * 延迟8m
     */
    LEVEL_12(12),
    /**
     * 延迟9m
     */
    LEVEL_13(13),
    /**
     * 延迟10m
     */
    LEVEL_14(14),
    /**
     * 延迟20m
     */
    LEVEL_15(15),
    /**
     * 延迟30m
     */
    LEVEL_16(16),
    /**
     * 延迟1h
     */
    LEVEL_17(17),
    /**
     * 延迟2h
     */
    LEVEL_18(18);

    private final int level;

    /**
     * Gets level.
     *
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    DelayLevel(int level) {
        this.level = level;
    }
}
