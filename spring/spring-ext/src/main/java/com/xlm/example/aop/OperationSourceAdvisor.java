package com.xlm.example.aop;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.beans.factory.InitializingBean;

public class OperationSourceAdvisor<T> extends AbstractBeanFactoryPointcutAdvisor implements InitializingBean {
    private OperationSource<T> operationSource;
    private ArgumentsOperationSource<T> argumentsOperationSource;
    private OperationSourceMethodPoint<T> sourceMethodPoint;

    @Override
    public void afterPropertiesSet() throws Exception {
        sourceMethodPoint = new OperationSourceMethodPoint<>(operationSource, argumentsOperationSource);
    }

    @Override
    public Pointcut getPointcut() {
        return sourceMethodPoint;
    }

    /**
     * Set the cache operation attribute source which is used to find cache
     * attributes. This should usually be identical to the source reference
     * set on the cache interceptor itself.
     */
    public void setOperationSource(OperationSource<T> source) {
        this.operationSource = source;
    }


    public void setArgumentsOperationSource(ArgumentsOperationSource<T> argumentsOperationSource) {
        this.argumentsOperationSource = argumentsOperationSource;
    }

    /**
     * Set the {@link ClassFilter} to use for this pointcut.
     * Default is {@link ClassFilter#TRUE}.
     */
    public void setClassFilter(ClassFilter classFilter) {
        this.sourceMethodPoint.setClassFilter(classFilter);
    }
}
