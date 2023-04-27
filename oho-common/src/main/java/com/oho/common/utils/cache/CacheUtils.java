package com.oho.common.utils.cache;

import cn.hutool.cache.CacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 缓存工具类
 *
 * @author Sparkler
 * @createDate 2023/2/14-12:17
 */
@EnableCaching
@Slf4j
@Configuration
@Component
public class CacheUtils extends CacheUtil {


}
