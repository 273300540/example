package com.xlm.example.aop;

import com.xlm.example.ObjectsKey;
import com.xlm.example.lock.LockAspectSupport;
import com.xlm.example.lock.LockSource;
import com.xlm.example.spel.MethodExpressionRootObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AspectSupport<T> implements BeanFactoryAware {
    private static Logger logger = LoggerFactory.getLogger(LockAspectSupport.class);
    private OperationSource<List<T>> operationSource;
    private ArgumentsOperationSource<List<T>> argumentsOperationSource;
    private final SpelExpressionParser lockSpelExpressionParser = new SpelExpressionParser();
    private ConcurrentHashMap<ObjectsKey, Expression> expressionConcurrentHashMap = new ConcurrentHashMap<>(16);
    protected BeanFactory beanFactory;

    private final ParameterNameDiscoverer paramNameDiscoverer = new DefaultParameterNameDiscoverer();


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public List<T> getOperationSource(Method method, Class targetClass, Object target, Object[] args) {
        List<T> source = operationSource.getOperationSources(method, targetClass);
        List<T> argSource = null;
        if (argumentsOperationSource != null) {
            argSource = argumentsOperationSource.getOperationSources(method, targetClass, target, args);
        }
        if (source == null) {
            return argSource;
        }
        if (argSource == null) {
            return source;
        }
        ArrayList list = new ArrayList<>(source.size() + argSource.size());
        list.addAll(source);
        list.addAll(argSource);
        Collections.sort(list, Comparator.comparingInt(LockSource::getOrder));//TODO 归并排序
        return list;
    }

    protected String parse(String expression, Method method, Class targetClass, Object[] args, Object target) {
        ObjectsKey key = new ObjectsKey(targetClass, method, expression);
        Expression spExpression = expressionConcurrentHashMap.computeIfAbsent(key, oneKey -> lockSpelExpressionParser.parseExpression(expression));
        MethodExpressionRootObject rootObject = new MethodExpressionRootObject(method, targetClass, args, target);
        MethodBasedEvaluationContext context = new MethodBasedEvaluationContext(rootObject, method, args, paramNameDiscoverer);
        return (String) spExpression.getValue(context);
    }

    public OperationSource<List<T>> getOperationSource() {
        return operationSource;
    }

    public void setOperationSource(OperationSource<List<T>> operationSource) {
        this.operationSource = operationSource;
    }

    public ArgumentsOperationSource<List<T>> getArgumentsOperationSource() {
        return argumentsOperationSource;
    }

    public void setArgumentsOperationSource(ArgumentsOperationSource<List<T>> argumentsOperationSource) {
        this.argumentsOperationSource = argumentsOperationSource;
    }
}
