package com.tauren.boss.config;

import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis����
 * 
 * @author niuyandong
 * @since 2017��9��23������3:16:13
 */
@Configuration
@Import(MySQLDBConfig.class)
@EnableTransactionManagement(order = 0)
@MapperScan("com.tauren.boss.dao.mapper")
public class MyBatisConfig {
    
    @Resource
    private DataSource dataSource;
    
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean.getObject();
    }
}
