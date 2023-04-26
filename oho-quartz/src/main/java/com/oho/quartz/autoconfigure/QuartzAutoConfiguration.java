package com.oho.quartz.autoconfigure;

import com.oho.quartz.core.utils.QuartzJobUtil;
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