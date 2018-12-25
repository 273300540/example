package com.xlm.example.bpp;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

public class ValueAppendAnnotationInjectBeanPostProcessor extends AbstractAnnotationInjectBeanPostProcessor {
    @Override
    protected InjectionMetadata.InjectedElement doWithField(Field field, String beanName, Class<?> beanType, RootBeanDefinition beanDefinition) {
        AnnotationAttributes annotationAttributes = AnnotatedElementUtils.getMergedAnnotationAttributes(field, ValueAppend.class);
        if (annotationAttributes == null) {
            return null;
        }
        ValueAppendInjectedElement valueAppendInjectedElement = new ValueAppendInjectedElement(field, null);
        valueAppendInjectedElement.annotationAttributes = annotationAttributes;
        return valueAppendInjectedElement;

    }

    @Override
    protected InjectionMetadata.InjectedElement doWithMethod(Method method, String beanName, Class<?> beanType, RootBeanDefinition beanDefinition) {
        //ReflectionUtils.findMethod(beanType,)
        return null;
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
            throw new IllegalArgumentException(
                    "ValueAppendAnnotationInjectBeanPostProcessor requires a ConfigurableListableBeanFactory: " + beanFactory);
        }
        super.setBeanFactory(beanFactory);
    }


    public class ValueAppendInjectedElement extends InjectionMetadata.InjectedElement {
        AnnotationAttributes annotationAttributes;
        String value;
        Method getMethod;

        protected ValueAppendInjectedElement(Member member, PropertyDescriptor pd) {
            super(member, pd);
        }

        /**
         * Either this or {@link #inject} needs to be overridden.
         */
        @Nullable
        protected Object getResourceToInject(Object target, @Nullable String requestingBeanName) {
            try {
                if (isField) {
                    return getResourceToInject(target, (Field) member, requestingBeanName);
                } else {
                    return getResourceToInject(target, (Method) member, requestingBeanName);
                }
            } catch (Exception e) {
                throw new BeanCreationException("", e);
            }
        }

        protected Object getResourceToInject(Object target, Field field, @Nullable String requestingBeanName) throws IllegalAccessException {
            if (StringUtils.isEmpty(value)) {
                value = (String) ((Field) member).get(target);
            }

            ConfigurableListableBeanFactory factory = (ConfigurableListableBeanFactory) beanFactory;
            String str = factory.resolveEmbeddedValue(annotationAttributes.getString("value"));
            return annotationAttributes.getBoolean("prefix") ? str + value : value + str;
        }

        protected Object getResourceToInject(Object target, Method method, @Nullable String requestingBeanName) throws InvocationTargetException, IllegalAccessException {
            if (StringUtils.isEmpty(value)) {
                value = (String) method.invoke(target);

            }
            ConfigurableListableBeanFactory factory = (ConfigurableListableBeanFactory) beanFactory;
            String str = factory.resolveEmbeddedValue(annotationAttributes.getString("value"));
            return annotationAttributes.getBoolean("prefix") ? str + value : value + str;
        }
    }
}