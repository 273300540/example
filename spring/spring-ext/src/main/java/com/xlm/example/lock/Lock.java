package com.xlm.example.lock;

import com.xlm.example.Factory;
import com.xlm.example.spel.MethodExpressionRootObject;

import java.lang.annotation.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Repeatable(value = Lock.LockList.class)
public @interface Lock {
    String name();

    String factoryName();

    int order() default 0;

    int timeOut() default 0;

    TimeUnit timeUnit() default TimeUnit.SECONDS;

    boolean expression() default false;

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @Documented
    @interface LockList {
        Lock[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @Documented
    @interface LockSupplier {
        Class<? extends  Factory<List<LockSource>, MethodExpressionRootObject>> value();
    }
}
