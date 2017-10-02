package com.tauren.boss.dao.mapper;

import javax.annotation.Resource;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tauren.boss.BaseTest;

/**
 * 
 * @author niuyandong
 * @since 2017��9��23������4:33:34
 */
public class MenuMapperTest extends BaseTest {
    
    private static final Logger logger = LoggerFactory.getLogger(MenuMapperTest.class);
    
    @Resource
    private MenuMapper menuMapper;
    
    @Test
    public void testFind() {
        logger.info("==>���Կ�ʼ");
        logger.info(menuMapper.find().toString());
        logger.info("==>���Խ���");
    }
}
