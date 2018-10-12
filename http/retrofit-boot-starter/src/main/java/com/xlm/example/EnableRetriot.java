package com.xlm.example;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(RetriotBeanDefinitionRegistrar.class)
public @interface EnableRetriot {
    boolean bind() default true;

    String[] basePackage() default {};

    String referenceName() default "";

}
