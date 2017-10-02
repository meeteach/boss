package com.tauren.boss.dao.mapper;

import javax.annotation.Resource;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tauren.boss.BaseTest;

/**
 * 
 * @author niuyandong
 * @since 2017年9月23日下午4:33:34
 */
public class MenuMapperTest extends BaseTest {
    
    private static final Logger logger = LoggerFactory.getLogger(MenuMapperTest.class);
    
    @Resource
    private MenuMapper menuMapper;
    
    @Test
    public void testFind() {
        logger.info("==>测试开始");
        logger.info(menuMapper.find().toString());
        logger.info("==>测试结束");
    }
}
