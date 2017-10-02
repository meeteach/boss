package com.tauren.boss.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Controller;

/**
 * Spring上下文配置
 * 
 * @author niuyandong
 * @since 2017年9月23日上午12:15:29
 */
@Configuration
@EnableAspectJAutoProxy
@Import({MyBatisConfig.class})
@ComponentScan(basePackages = "com.tauren.boss", excludeFilters = {@Filter(Controller.class), @Filter(Configuration.class)})
public class SpringContextConfig {
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    
}
