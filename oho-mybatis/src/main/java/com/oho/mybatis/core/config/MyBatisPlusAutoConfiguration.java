package com.oho.mybatis.core.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author MENGJIAO
 */
@Slf4j
@Configuration
@ConditionalOnProperty(value = "mybatis-plus.type-aliases-package")
@ComponentScan("com.oho.mybatis")
@MapperScan("com.oho.mybatis.core.mapper")
public class MyBatisPlusAutoConfiguration {

}
