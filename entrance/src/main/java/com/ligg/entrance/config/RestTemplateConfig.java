package com.ligg.entrance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @Author Ligg
 * @Time 2025/6/16
 * RestTemplate配置类
 */
@Configuration
public class RestTemplateConfig {
    /**
     * 创建RestTemplate实例
     *
     * @return RestTemplate实例
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}