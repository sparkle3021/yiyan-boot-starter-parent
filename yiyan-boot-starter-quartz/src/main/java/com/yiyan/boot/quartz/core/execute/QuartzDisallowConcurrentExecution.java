package com.yiyan.boot.quartz.core.execute;

import com.yiyan.boot.quartz.core.model.AbstractQuartzJob;
import com.yiyan.boot.quartz.core.model.dto.QuartzJobParam;
import com.yiyan.boot.quartz.core.utils.JobInvokeUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（禁止并发执行）
 *
 * @author ruoyi
 */
@DisallowConcurrentExecution
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, QuartzJobParam quartzJobParam) throws Exception {
        JobInvokeUtil.invokeMethod(quartzJobParam);
    }
}
