package com.yiyan.boot.cache.core.config;

import java.util.Locale;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程池信息
 */
public class NamedThreadFactory implements ThreadFactory {
    private static final AtomicInteger threadPoolNumber = new AtomicInteger(1);
    private static final String NAME_PATTERN = "%s-%d-thread";
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String threadNamePrefix;

    public NamedThreadFactory(String threadNamePrefix) {
        SecurityManager s = System.getSecurityManager();
        this.group = s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.threadNamePrefix = String.format(Locale.ROOT, "%s-%d-thread", checkPrefix(threadNamePrefix), threadPoolNumber.getAndIncrement());
    }

    /**
     * 线程名前缀检查，如参数为空默认‘tms’
     *
     * @param prefix 前缀
     * @return String
     */
    private static String checkPrefix(String prefix) {
        return prefix != null && prefix.length() != 0 ? prefix : "gemini-cache";
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(this.group, r, String.format(Locale.ROOT, NAME_PATTERN, this.threadNamePrefix, this.threadNumber.getAndIncrement()), 0L);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}
