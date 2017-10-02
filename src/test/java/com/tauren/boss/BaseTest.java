package com.tauren.boss;

import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.tauren.boss.config.SpringContextConfig;

/**
 * ���Ի�����
 * 
 * @author niuyandong
 * @since 2017��9��23������4:29:38
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringContextConfig.class})
@Rollback
public abstract class BaseTest {
    
}
