package com.xlm.example.service;

import com.xlm.example.Factory;
import com.xlm.example.lock.Lock;
import com.xlm.example.lock.LockSource;
import com.xlm.example.spel.MethodExpressionRootObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceTestImpl implements ServiceTest {
    @Override
    @Lock(name = "'test'", factoryName = "'test'", timeOut = 5, order = -1)
    @Lock(name = "test2", factoryName = "test", timeOut = 5)
    @Lock(name = "#name", factoryName = "#factory", timeOut = 5,expression = true)
    public String testAop(String name, String factory) {
        try {
            Thread.sleep(3000L);
        } catch (Exception e) {

        }
        System.out.println("tttttttttttttttttttttttttt");
        return "ttttttt";
    }

    @Override
    @Lock.LockSupplier(TestFactory.class)
    public String testSupplier(int i) {
        System.out.println("=====================testSupplier======================");
        return "testSupplier";
    }

    public static class TestFactory implements Factory<List<LockSource>, MethodExpressionRootObject> {
        @Override
        public List<LockSource> create(MethodExpressionRootObject name) {
            int size= (Integer) name.getArgs()[0];
            ArrayList<LockSource> list = new ArrayList<>(size);
            for (int i =0; i < size; i++) {
                LockSource one = new LockSource();


                one.setExpression(i%2==0);
                if(one.isExpression()){
                    one.setFactoryName("'factoryTest" + i+"'");
                    one.setName("'test" + i+"'");
                }else {
                    one.setFactoryName("notFactoryTest"+i);
                    one.setName("notTest" + i);
                }
                one.setTimeOut(i + 1);
                list.add(one);
            }
            return list;
        }
    }
}
