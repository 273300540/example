package com.xlm.example;

import static org.junit.Assert.assertTrue;

import com.xlm.example.mapper.UserLevelMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Unit test for simple App.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:account-db.xml")
public class AppTest {
    @Resource
    UserLevelMapper userLevelMapper;

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        Integer start = 10000001;
        int size = 10;
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i]=start+i;
        }
        userLevelMapper.insertLevel(array);
    }
}
