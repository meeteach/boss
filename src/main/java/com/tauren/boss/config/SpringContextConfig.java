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
 * Spring����������
 * 
 * @author niuyandong
 * @since 2017��9��23������12:15:29
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
