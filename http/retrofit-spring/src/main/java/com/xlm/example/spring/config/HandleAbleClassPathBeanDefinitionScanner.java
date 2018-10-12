package com.xlm.example.spring.config;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;

public class HandleAbleClassPathBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {
    Function<BeanDefinitionHolder, Boolean> beanDefinitionHandleFunction;

    public HandleAbleClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public HandleAbleClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }

    public HandleAbleClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment) {
        super(registry, useDefaultFilters, environment);
    }

    public HandleAbleClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment,
                                                    ResourceLoader resourceLoader) {
        super(registry, useDefaultFilters, environment, resourceLoader);
    }

    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> set = super.doScan(basePackages);
        if (set != null) {
            Iterator<BeanDefinitionHolder> iterator = set.iterator();
            while (iterator.hasNext()) {
                BeanDefinitionHolder holder = iterator.next();
                if (!beanDefinitionHandleFunction.apply(holder)) {
                    iterator.remove();
                }
            }
        }
        return set;
    }


    public Function<BeanDefinitionHolder, Boolean> getBeanDefinitionHandleFunction() {
        return beanDefinitionHandleFunction;
    }

    public void setBeanDefinitionHandleFunction(Function<BeanDefinitionHolder, Boolean> beanDefinitionHandleFunction) {
        this.beanDefinitionHandleFunction = beanDefinitionHandleFunction;
    }



    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return true;
    }
}
