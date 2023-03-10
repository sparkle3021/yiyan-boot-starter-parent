package com.oho.quartz.config;

import com.oho.quartz.core.QuartzJobUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Sparkler
 * @createDate 2023/1/14
 */
@Configuration
@Import({QuartzJobUtil.class})
public class QuartzAutoConfiguration {

}