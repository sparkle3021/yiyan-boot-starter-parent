package com.oho.quartz.core.model.contant;

/**
 * 定时任务常量 -- 参考若依 [<a href="https://gitee.com/y_project/RuoYi-Vue">...</a>]
 *
 * @author Sparkler
 * @createDate 2023/1/13
 */
public class ScheduleConstants {

    /**
     * 执行目标key
     */
    public static final String TASK_PROPERTIES = "TASK_PROPERTIES";

    /**
     * 默认
     */
    public static final String MISFIRE_DEFAULT = "0";

    /**
     * 立即触发执行
     */
    public static final String MISFIRE_IGNORE_MISFIRES = "1";

    /**
     * 触发一次执行
     */
    public static final String MISFIRE_FIRE_AND_PROCEED = "2";

    /**
     * 不触发立即执行
     */
    public static final String MISFIRE_DO_NOTHING = "3";

    /**
     * 任务允许并发
     */
    public static final String CONCURRENT = "0";

    /**
     * 任务不允许并发
     */
    public static final String NOT_CONCURRENT = "1";

    public enum Status {
        /**
         * 正常
         */
        NORMAL("0"),
        /**
         * 暂停
         */
        PAUSE("1");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
