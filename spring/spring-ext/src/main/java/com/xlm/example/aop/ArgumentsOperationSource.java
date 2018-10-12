package com.xlm.example.aop;

import java.lang.reflect.Method;

public interface ArgumentsOperationSource<T> {
    T getOperationSources(Method method, Class targetClass, Object target, Object[] args);

    boolean match(Method method, Class targetClass);
}
