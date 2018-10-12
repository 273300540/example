package com.xlm.example;

import com.xlm.example.service.ServiceTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Unit test for simple App.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({/*"classpath:account-db.xml",*/"classpath:context.xml"})
public class AppTest {
    @Resource
    private ServiceTest test;
    private volatile  boolean await =true;
    String name ="name";
    String factoryName ="factoryName";

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        new Thread() {
            public void run() {
                try {
                    test.testAop(name,factoryName);
                } finally {
                    await = false;
                }


            }
        }.start();
        test.testAop(name,factoryName);
        while (await) {

        }

    }

    @Test
    public void testArgumentLock() {
        new Thread() {
            public void run() {
                try {
                    test.testSupplier(2);
                } finally {
                    await = false;
                }


            }
        }.start();
        test.testSupplier(2);
        while (await) {

        }

    }
}
