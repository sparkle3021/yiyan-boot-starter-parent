package com.oho.quartz.core.model;

import com.oho.common.utils.BeanUtils;
import com.oho.quartz.core.model.contant.ScheduleConstants;
import com.oho.quartz.core.model.dto.QuartzJobParam;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.util.Date;

/**
 * 抽象quartz调用 -- 取自若依 [<a href="https://gitee.com/y_project/RuoYi-Vue">...</a>]
 *
 * @author Sparkler
 * @createDate 2023/1/13
 */
@Slf4j
public abstract class AbstractQuartzJob implements Job {

    /**
     * 线程本地变量
     */
    private static ThreadLocal<Date> threadLocal = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) {
        QuartzJobParam quartzJobParam = BeanUtils.copyProperties(context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES), QuartzJobParam::new);
        try {
            // before(context, quartzJobParam);
            if (quartzJobParam != null) {
                doExecute(context, quartzJobParam);
            }
            // after(context, quartzJobParam, null);
        } catch (Exception e) {
            log.error("任务执行异常  - ：", e);
            after(context, quartzJobParam, e);
        }
    }

    /**
     * 执行前
     *
     * @param context        工作执行上下文对象
     * @param quartzJobParam 系统计划任务
     */
    protected void before(JobExecutionContext context, QuartzJobParam quartzJobParam) {
        threadLocal.set(new Date());
    }

    /**
     * 执行后
     *
     * @param context        工作执行上下文对象
     * @param quartzJobParam 系统计划任务
     */
    protected void after(JobExecutionContext context, QuartzJobParam quartzJobParam, Exception exception) {
        threadLocal.remove();
    }

    /**
     * 执行方法，由子类重载
     *
     * @param context        工作执行上下文对象
     * @param quartzJobParam 系统计划任务
     * @throws Exception 执行过程中的异常
     */
    protected abstract void doExecute(JobExecutionContext context, QuartzJobParam quartzJobParam) throws Exception;
}
