package com.oho.mybatis.core.config;

import com.oho.mybatis.model.constant.ConfigKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author MENGJIAO
 */
@Slf4j
@Configuration
public class MyBatisBatchThreadPool {

    @Bean(name = "mybatisBatchExecutor")
    public Executor asyncServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(ConfigKey.MAX_BATCH_THREAD_SIZE);
        //配置最大线程数
        executor.setMaxPoolSize(ConfigKey.MAX_BATCH_THREAD_SIZE);
        //配置队列大小
        executor.setQueueCapacity(ConfigKey.MAX_BATCH_THREAD_SIZE * 4);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("MYBATIS-BATCH-");

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
}
