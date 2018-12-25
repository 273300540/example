package com.xlm.example.bpp;

import com.xlm.example.MapCache;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAnnotationInjectBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware, MergedBeanDefinitionPostProcessor {
    protected BeanFactory beanFactory;
    protected MapCache<String, InjectionMetadata> mapCache = new MapCache();

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
        InjectionMetadata metadata = mapCache.getValue(beanName, name -> {
            return buildInjectionMetadata(beanDefinition, beanType, beanName);
        });
        if(metadata == null){
            return ;
        }
        metadata.checkConfigMembers(beanDefinition);
    }

    public InjectionMetadata buildInjectionMetadata(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
        List<InjectionMetadata.InjectedElement> injectedElementList = new ArrayList<>();
        ReflectionUtils.doWithFields(beanType, field -> {
            InjectionMetadata.InjectedElement injectedElement = doWithField(field, beanName, beanType, beanDefinition);
            if (injectedElement != null) {
                injectedElementList.add(injectedElement);
            }
        });
        ReflectionUtils.doWithMethods(beanType, method -> {
            InjectionMetadata.InjectedElement injectedElement = doWithMethod(method, beanName, beanType, beanDefinition);
            if (injectedElement != null) {
                injectedElementList.add(injectedElement);
            }
        });
        if (!injectedElementList.isEmpty()) {
            return new InjectionMetadata(beanType, injectedElementList);
        }
        return null;
    }

    protected abstract InjectionMetadata.InjectedElement doWithField(Field field, String beanName, Class<?> beanType, RootBeanDefinition beanDefinition);

    protected abstract InjectionMetadata.InjectedElement doWithMethod(Method field, String beanName, Class<?> beanType, RootBeanDefinition beanDefinition);

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        InjectionMetadata metadata = mapCache.getValue(beanName);
        if (metadata != null) {
            try {
                metadata.inject(bean, beanName, pvs);
            } catch (Throwable ex) {
                throw new BeanCreationException(beanName, "Injection of autowired dependencies failed", ex);
            }
        }
        return pvs;
    }


}
