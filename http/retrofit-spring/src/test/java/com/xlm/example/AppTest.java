package com.xlm.example;

import static org.junit.Assert.assertTrue;

import com.xlm.example.http.Contributor;
import com.xlm.example.http.HttpTest;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest {

    /**config.xml
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public  void test(){
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:/config.xml");
        String[] nameArray ={"httpTestOne","httpTestTwo"};
        for(String name :nameArray){
            HttpTest httpTest = (HttpTest)classPathXmlApplicationContext.getBean(name);
            List<Contributor> contributors = httpTest.contributors("square", "retrofit");
            System.out.println("==========="+name);
            System.out.println(contributors);
        }

    }
}
