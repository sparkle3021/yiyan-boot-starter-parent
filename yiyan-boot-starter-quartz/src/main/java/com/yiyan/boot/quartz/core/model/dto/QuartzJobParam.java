package com.yiyan.boot.quartz.core.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yiyan.boot.common.utils.StringUtils;
import com.yiyan.boot.quartz.core.model.contant.ScheduleConstants;
import com.yiyan.boot.quartz.core.utils.CronUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务属性
 *
 * @author Sparkler
 * @createDate 2023/1/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuartzJobParam implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务组名
     */
    private String jobGroup;

    /**
     * 调用目标字符串
     * 1. 如目标类被Spring 管理，则可使用bean名加方法名的方式。如：taskBean.fun1
     * 2. 如目标类不被Spring 管理，则可使用全类名名加方法名的方式。如：com.task.TaskBean.fun1
     * 3. 如果目标方法携带参数则通过：taskBean.fun1('params1',3L,2.5D)
     * * String字符串类型，以'或"开头; boolean布尔类型，等于true或者false; long长整形，以L结尾; double浮点类型，以D结尾; 其他类型归类为整形
     */
    private String invokeTarget;

    /**
     * cron执行表达式
     */
    private String cronExpression;

    /**
     * cron计划策略 "0=默认,1=立即触发执行,2=触发一次执行,3=不触发立即执行"
     */
    private String misfirePolicy = ScheduleConstants.MISFIRE_DEFAULT;

    /**
     * 是否并发执行（0允许 1禁止） 默认禁止
     */
    private String concurrent = ScheduleConstants.NOT_CONCURRENT;

    /**
     * 任务状态（0正常 1暂停），默认创建后启动
     */
    private String status = ScheduleConstants.Status.NORMAL.getValue();

    @NotBlank(message = "任务名称不能为空")
    @Size(max = 64, message = "任务名称不能超过64个字符")
    public String getJobName() {
        return jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    @NotBlank(message = "调用目标字符串不能为空")
    @Size(max = 500, message = "调用目标字符串长度不能超过500个字符")
    public String getInvokeTarget() {
        return invokeTarget;
    }

    @NotBlank(message = "Cron执行表达式不能为空")
    @Size(max = 255, message = "Cron执行表达式不能超过255个字符")
    public String getCronExpression() {
        return cronExpression;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getNextValidTime() {
        if (StringUtils.isNotEmpty(cronExpression)) {
            return CronUtils.getNextExecution(cronExpression);
        }
        return null;
    }

    public QuartzJobParam(String jobName, String jobGroup) {
        this.jobName = jobName;
        this.jobGroup = jobGroup;
    }
}
