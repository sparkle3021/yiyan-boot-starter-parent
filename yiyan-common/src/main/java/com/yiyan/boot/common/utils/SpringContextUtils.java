package com.yiyan.boot.common.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring上下文工具类
 *
 * @author Sparkler
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    /**
     * spring应用上下文
     */
    private static ApplicationContext applicationContext;

    /**
     * 注册bean
     *
     * @param beanName bean名称
     * @param bean     bean对象
     */
    public static void registerBean(String beanName, Object bean) {
        applicationContext.getAutowireCapableBeanFactory().autowireBean(bean);
        applicationContext.getAutowireCapableBeanFactory().initializeBean(bean, beanName);
    }

    /**
     * Gets application context.
     *
     * @return ApplicationContext application context
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取对象
     *
     * @param name the name
     * @return Object bean
     * @throws BeansException the beans exception
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    /**
     * 获取对象
     *
     * @param <T>   the type parameter
     * @param clazz the clazz
     * @return Object bean
     * @throws BeansException the beans exception
     */
    public static <T> T getBean(Class<T> clazz) throws BeansException {
        return applicationContext.getBean(clazz);
    }

    /**
     * 获取当前配置文件
     *
     * @return string [ ]
     */
    public String[] getProfiles() {
        return applicationContext.getEnvironment().getActiveProfiles();
    }

    /**
     * 实现ApplicationContextAware接口的回调方法。设置上下文环境
     */
    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) {
        SpringContextUtils.applicationContext = applicationContext;
    }
}
