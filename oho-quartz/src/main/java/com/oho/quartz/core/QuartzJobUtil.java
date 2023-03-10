package com.oho.quartz.core;

import com.oho.quartz.core.execute.QuartzDisallowConcurrentExecution;
import com.oho.quartz.core.execute.QuartzJobExecution;
import com.oho.quartz.core.model.contant.ScheduleConstants;
import com.oho.quartz.core.model.dto.QuartzJobParam;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Quartz 工具类.
 *
 * @author Sparkler
 * @createDate 2023 /1/13
 */
@Slf4j
@Component
public class QuartzJobUtil {

    @Autowired
    private Scheduler scheduler;

    /**
     * 构建任务触发对象
     *
     * @param jobName   the job name
     * @param groupName the group name
     * @return the trigger key
     */
    public static TriggerKey getTriggerKey(String jobName, String groupName) {
        return TriggerKey.triggerKey(jobName, groupName);
    }

    /**
     * 构建任务键对象
     *
     * @param jobName   the job name
     * @param groupName the group name
     * @return the job key
     */
    public static JobKey getJobKey(String jobName, String groupName) {
        return JobKey.jobKey(jobName, groupName);
    }

    /**
     * 设置定时任务策略
     *
     * @param job the job
     * @param cb  the cb
     * @return the cron schedule builder
     */
    public static CronScheduleBuilder handleCronScheduleMisfirePolicy(QuartzJobParam job, CronScheduleBuilder cb) {
        switch (job.getMisfirePolicy()) {
            case ScheduleConstants.MISFIRE_IGNORE_MISFIRES:
                return cb.withMisfireHandlingInstructionIgnoreMisfires();
            case ScheduleConstants.MISFIRE_FIRE_AND_PROCEED:
                return cb.withMisfireHandlingInstructionFireAndProceed();
            case ScheduleConstants.MISFIRE_DO_NOTHING:
                return cb.withMisfireHandlingInstructionDoNothing();
            default:
                return cb;
        }
    }

    /**
     * 得到quartz任务类
     *
     * @param quartzJobParam 执行计划
     * @return 具体执行任务类
     */
    private static Class<? extends Job> getQuartzJobClass(QuartzJobParam quartzJobParam) {
        boolean isConcurrent = ScheduleConstants.CONCURRENT.equals(quartzJobParam.getConcurrent());
        return isConcurrent ? QuartzJobExecution.class : QuartzDisallowConcurrentExecution.class;
    }

    /**
     * 创建定时任务
     *
     * @param quartzJobParam the quartz job param
     * @throws SchedulerException the scheduler exception
     */
    public void createScheduleJob(QuartzJobParam quartzJobParam) throws SchedulerException {
        Class<? extends Job> jobClass = getQuartzJobClass(quartzJobParam);
        // 构建job信息
        String jobGroup = quartzJobParam.getJobGroup();
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(getJobKey(quartzJobParam.getJobName(), jobGroup)).build();

        // 表达式调度构建器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartzJobParam.getCronExpression());
        cronScheduleBuilder = handleCronScheduleMisfirePolicy(quartzJobParam, cronScheduleBuilder);

        // 按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(quartzJobParam.getJobName(), jobGroup))
                .withSchedule(cronScheduleBuilder).build();

        // 放入参数，运行时的方法可以获取
        jobDetail.getJobDataMap().put(ScheduleConstants.TASK_PROPERTIES, quartzJobParam);

        // 判断是否存在
        if (scheduler.checkExists(getJobKey(quartzJobParam.getJobName(), jobGroup))) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(getJobKey(quartzJobParam.getJobName(), jobGroup));
        }

        // 判断任务是否过期
        if (Objects.nonNull(CronUtils.getNextExecution(quartzJobParam.getCronExpression()))) {
            // 执行调度任务
            scheduler.scheduleJob(jobDetail, trigger);
        }

        // 暂停任务
        if (quartzJobParam.getStatus().equals(ScheduleConstants.Status.PAUSE.getValue())) {
            scheduler.pauseJob(getJobKey(quartzJobParam.getJobName(), jobGroup));
        }
    }

    /**
     * 更新任务的时间表达式
     *
     * @param quartzJobParam 调度信息
     * @throws SchedulerException the scheduler exception
     */
    public void updateJob(QuartzJobParam quartzJobParam) throws SchedulerException {
        updateSchedulerJob(quartzJobParam);
    }

    /**
     * 更新任务
     *
     * @param quartzJobParam 任务对象
     * @throws SchedulerException the scheduler exception
     */
    public void updateSchedulerJob(QuartzJobParam quartzJobParam) throws SchedulerException {
        // 判断是否存在
        JobKey jobKey = getJobKey(quartzJobParam.getJobName(), quartzJobParam.getJobGroup());
        if (scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        createScheduleJob(quartzJobParam);
    }

    /**
     * 校验cron表达式是否有效
     *
     * @param cronExpression 表达式
     * @return 结果 boolean
     */
    public boolean checkCronExpressionIsValid(String cronExpression) {
        return CronUtils.isValid(cronExpression);
    }

    /**
     * 暂停任务.
     *
     * @param quartzJobParam the quartz job param
     * @throws SchedulerException the scheduler exception
     */
    public void pauseJob(QuartzJobParam quartzJobParam) throws SchedulerException {
        scheduler.pauseJob(getJobKey(quartzJobParam.getJobName(), quartzJobParam.getJobGroup()));
    }

    /**
     * 恢复任务.
     *
     * @param quartzJobParam the quartz job param
     * @throws SchedulerException the scheduler exception
     */
    public void resumeJob(QuartzJobParam quartzJobParam) throws SchedulerException {
        scheduler.resumeJob(getJobKey(quartzJobParam.getJobName(), quartzJobParam.getJobGroup()));
    }

    /**
     * 删除任务.
     *
     * @param quartzJobParam the quartz job param
     * @throws SchedulerException the scheduler exception
     */
    public void deleteJob(QuartzJobParam quartzJobParam) throws SchedulerException {
        scheduler.deleteJob(getJobKey(quartzJobParam.getJobName(), quartzJobParam.getJobGroup()));
    }

    /**
     * 立即运行任务.
     *
     * @param quartzJobParam the quartz job param
     * @return the boolean
     * @throws SchedulerException the scheduler exception
     */
    public Boolean run(QuartzJobParam quartzJobParam) throws SchedulerException {
        boolean result = false;
        JobKey jobKey = getJobKey(quartzJobParam.getJobName(), quartzJobParam.getJobGroup());
        JobDataMap jobDataMap = scheduler.getJobDetail(jobKey).getJobDataMap();
        if (scheduler.checkExists(jobKey)) {
            result = true;
            scheduler.triggerJob(jobKey, jobDataMap);
        }
        return result;
    }

    /**
     * 暂停所有任务
     *
     * @throws SchedulerException the scheduler exception
     */
    public void pauseAll() throws SchedulerException {
        scheduler.pauseAll();
    }

    /**
     * 恢复所有任务
     *
     * @throws SchedulerException the scheduler exception
     */
    public void resumeAll() throws SchedulerException {
        scheduler.resumeAll();
    }

    /**
     * 清除所有Quartz任务
     *
     * @throws SchedulerException the scheduler exception
     */
    public void clear() throws SchedulerException {
        scheduler.clear();
    }

    /**
     * 查询任务详情
     *
     * @param jobName      任务名
     * @param jobGroupName 任务组
     * @throws SchedulerException the scheduler exception
     */
    public JobDetail getJobDetail(String jobName, String jobGroupName) throws SchedulerException {
        JobKey jobKey = getJobKey(jobName, jobGroupName);
        log.info("jobkey : {}", jobKey.toString());
        return scheduler.getJobDetail(jobKey);
    }
}
