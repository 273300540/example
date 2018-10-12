package com.xlm.example.lock;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.Ordered;

public class LockMethodIntercptor extends LockAspectSupport implements MethodInterceptor, Ordered {
    private int order;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);
        return invokeWithLock(invocation.getMethod(), targetClass,invocation.getThis(),invocation.getArguments(), invocation::proceed);
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
