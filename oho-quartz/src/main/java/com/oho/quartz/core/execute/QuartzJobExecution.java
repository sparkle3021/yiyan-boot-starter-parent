package com.oho.quartz.core.execute;

import com.oho.quartz.core.model.AbstractQuartzJob;
import com.oho.quartz.core.model.dto.QuartzJobParam;
import com.oho.quartz.core.utils.JobInvokeUtil;
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
