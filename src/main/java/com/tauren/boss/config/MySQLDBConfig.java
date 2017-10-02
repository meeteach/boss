package com.tauren.boss.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * MySQL数据库配置
 * 
 * @author niuyandong
 * @since 2017年9月23日上午2:06:37
 */
@Configuration
@PropertySource("classpath:/mysqldb.properties")
public class MySQLDBConfig {
    
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.driverClassName}")
    private String driverClassName;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;
    @Value("${initialSize}")
    private int initialSize;
    @Value("${maxActive}")
    private int maxActive;
    @Value("${maxIdle}")
    private int maxIdle;
    @Value("${minIdle}")
    private int minIdle;
    
    @Bean(destroyMethod = "close")
    public javax.sql.DataSource dataSource() {
        
        PoolProperties p = new PoolProperties();
        p.setUrl(url);
        p.setDriverClassName(driverClassName);
        p.setUsername(username);
        p.setPassword(password);
        
        p.setInitialSize(initialSize);
        p.setMaxActive(maxActive);
        p.setMaxIdle(maxIdle);
        p.setMinIdle(minIdle);
        p.setMaxWait(10000);
        
        p.setTestOnBorrow(true);
        p.setTestWhileIdle(true);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        
        p.setValidationQuery("SELECT 1");
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setRemoveAbandonedTimeout(60);
        p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        
        DataSource ds = new DataSource();
        ds.setPoolProperties(p);
        return ds;
    }
    
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
