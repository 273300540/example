package com.xlm.example;

import com.xlm.example.http.HttpTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootStartUp.class)

public class TestBoot {
    @Resource
    private HttpTest httpTest;
    @Test
    public  void testHttp(){
        System.out.println(httpTest.contributors("square", "retrofit"));
    }
}
