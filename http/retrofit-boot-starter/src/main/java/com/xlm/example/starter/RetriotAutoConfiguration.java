package com.xlm.example.starter;

import com.xlm.example.spring.config.RetrofitConfig;
import com.xlm.example.spring.config.RetrofitConfigurator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ConditionalOnClass(Retrofit.class)
@EnableConfigurationProperties(RetriotProperties.class)
@AutoConfigureBefore(DispatcherServletAutoConfiguration.class)
public class RetriotAutoConfiguration implements BeanFactoryAware {
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
