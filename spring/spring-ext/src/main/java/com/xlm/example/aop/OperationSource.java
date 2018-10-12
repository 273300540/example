package com.xlm.example.aop;

import java.lang.reflect.Method;
import java.util.Collection;

public interface OperationSource<T> {
    T getOperationSources(Method method, Class targetClass);
}
