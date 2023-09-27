package com.yiyan.boot.quartz.core.execute;

import com.yiyan.boot.quartz.core.utils.JobInvokeUtil;
import com.yiyan.boot.quartz.core.model.AbstractQuartzJob;
import com.yiyan.boot.quartz.core.model.dto.QuartzJobParam;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（允许并发执行）
 *
 * @author ruoyi
 */
public class QuartzJobExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, QuartzJobParam quartzJobParam) throws Exception {
        JobInvokeUtil.invokeMethod(quartzJobParam);
    }
}
