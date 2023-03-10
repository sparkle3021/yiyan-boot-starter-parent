package com.oho.elasticsearch.config;

import com.oho.elasticsearch.core.ElasticSearchUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

/**
 * Elasticsearch 配置类
 *
 * @author Sparkler
 * @createDate 2023/1/6
 */
@Slf4j
@Configuration
@ConditionalOnClass({ElasticsearchRestTemplate.class})
@Import({ElasticSearchUtil.class})
public class ElasticSearchAutoConfiguration {

}