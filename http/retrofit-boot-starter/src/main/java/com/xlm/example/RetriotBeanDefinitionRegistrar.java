package com.xlm.example;

import com.xlm.example.starter.RetriotProperties;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.annotation.Annotation;
import java.util.Map;


public class RetriotBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        Map<String, Object> map = importingClassMetadata.getAnnotationAttributes(EnableRetriot.class.getName());

        RetrofitConfigurators configurators = generateRetrofit(map);
        configurators.init(registry);
        configurators.process(registry);


    }

    private void bind(RetriotProperties bean) {
        ConfigurationProperties annotation = AnnotationUtils.findAnnotation(bean.getClass(), ConfigurationProperties.class);
        Binder.get(environment).bind(annotation.prefix(), Bindable.ofInstance(bean).withAnnotations().withAnnotations(new Annotation[]{annotation}));
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public RetrofitConfigurators generateRetrofit(Map<String, Object> map) {
        RetrofitConfigurators configurators = new RetrofitConfigurators();
        Boolean bind = (Boolean) map.getOrDefault("bind", true);
        if (bind) {
            RetriotProperties retriotProperties = new RetriotProperties();
            bind(retriotProperties);
            configurators.from(retriotProperties);
        }

        String[] basePackage = (String[]) map.get("basePackage");
        String referenceName = (String) map.get("referenceName");
        if (basePackage != null && basePackage.length > 0 && referenceName != null && referenceName.length() > 0) {
            configurators.addRetrofitConfigurator(basePackage, referenceName);
        }
        return configurators;
    }
}
