package com.xlm.example.lock;

import com.xlm.example.Factory;
import com.xlm.example.ObjectsKey;
import com.xlm.example.aop.AspectSupport;
import com.xlm.example.aop.OperationSource;
import com.xlm.example.spel.MethodExpressionRootObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;

public abstract class LockAspectSupport extends AspectSupport implements InitializingBean, BeanFactoryAware {
    private static Logger logger = LoggerFactory.getLogger(LockAspectSupport.class);

    private Factory<Factory<Lock, String>, String> lockManager;
    private String defaultName = "lockManager";


    @Override
    public void afterPropertiesSet() throws Exception {
        if (lockManager == null) {
            lockManager = (Factory<Factory<Lock, String>, String>) beanFactory.getBean(defaultName, Factory.class);
        }
    }

    public Object invokeWithLock(Method inMethod, Class targetClass, Object target, Object[] args, InvocationCallback supplier) throws Throwable {
        Method method = ClassUtils.getMostSpecificMethod(inMethod, targetClass);
        method = BridgeMethodResolver.findBridgedMethod(method);
        List<LockSource> lockSources = getOperationSource(inMethod, targetClass, target, args);
        List<java.util.concurrent.locks.Lock> locked = new ArrayList<>(lockSources.size());
        try {
            for (LockSource one : lockSources) {
                String factoryName = one.isExpression() ? parse(one.getFactoryName(), method, targetClass, args, target) : one.getFactoryName();
                Factory<Lock, String> factory = lockManager.create(factoryName);
                String lockName = one.isExpression() ? parse(one.getName(), method, targetClass, args, target) : one.getName();
                Lock lock = factory.create(lockName);

                if (one.getTimeOut() >= 0) {
                    logger.debug("factory={},lock={},LockSource={},try lock{}", factoryName, lockName, one, lock);
                    if (!lock.tryLock(one.getTimeOut(), one.getTimeUnit())) {
                        throw new TimeoutException("factory=" + factoryName + " lockName=" + lockName + " lock {" + one + "} time out");
                    }
                } else {
                    logger.debug("factory={},lock={},LockSource={}, lock{}", factoryName, lockName, one, lock);
                    lock.lock();
                }
                locked.add(lock);
            }
            return supplier.proceedWithInvocation();
        } finally {
            for (Lock one : locked) {
                logger.debug("unlock,{}", one);
                one.unlock();
            }
        }
    }


    public Factory<Factory<Lock, String>, String> getLockManager() {
        return lockManager;
    }

    public void setLockManager(Factory<Factory<Lock, String>, String> lockManager) {
        this.lockManager = lockManager;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    @FunctionalInterface
    protected interface InvocationCallback {

        Object proceedWithInvocation() throws Throwable;
    }
}
