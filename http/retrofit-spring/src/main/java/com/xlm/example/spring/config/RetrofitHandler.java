package com.xlm.example.spring.config;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import retrofit2.Retrofit;

import java.util.function.Function;

public class RetrofitHandler implements Function<BeanDefinitionHolder, Boolean> {
    private Retrofit retrofit;
    private String retrofitName;


    public RetrofitHandler(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public RetrofitHandler(String retrofitName) {
        this.retrofitName = retrofitName;
    }

    @Override
    public Boolean apply(BeanDefinitionHolder beanDefinitionHolder) {
        String interfaceName = beanDefinitionHolder.getBeanDefinition().getBeanClassName();
        beanDefinitionHolder.getBeanDefinition().setBeanClassName("com.xlm.example.spring.config.RetrofitFactoryBean");
        if(retrofitName!=null && retrofitName.length()>0) {
            beanDefinitionHolder.getBeanDefinition().getPropertyValues().add("retrofit", new RuntimeBeanReference(retrofitName));
        }else{
            beanDefinitionHolder.getBeanDefinition().getPropertyValues().add("retrofit", retrofit);

        }
        beanDefinitionHolder.getBeanDefinition().getPropertyValues().add("interfaceName", interfaceName);
        return true;
    }
}
