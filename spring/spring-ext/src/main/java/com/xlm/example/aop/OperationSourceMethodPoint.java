package com.xlm.example.aop;

import com.xlm.example.aop.OperationSource;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;

public class OperationSourceMethodPoint<T> extends StaticMethodMatcherPointcut {
    private OperationSource<T> operationSource;
    private ArgumentsOperationSource<T> argumentsOperationSource;

    public OperationSourceMethodPoint(OperationSource<T> operationSource) {
        this.operationSource = operationSource;
    }

    public OperationSourceMethodPoint(OperationSource<T> operationSource, ArgumentsOperationSource argumentsOperationSource) {
        this(operationSource);
        this.argumentsOperationSource = argumentsOperationSource;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return operationSource != null && operationSource.getOperationSources(method, targetClass) != null
                ||(argumentsOperationSource != null ? argumentsOperationSource.match(method, targetClass) : false);
    }

}
