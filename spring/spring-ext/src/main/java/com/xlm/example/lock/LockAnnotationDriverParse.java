package com.xlm.example.lock;

import com.xlm.example.aop.OperationSourceAdvisor;
import org.springframework.aop.config.AopNamespaceUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;


public class LockAnnotationDriverParse implements BeanDefinitionParser {
    private static final String LOCK_ADVISOR_BEAN_NAME = "com.xlm.example.lock.internalLockAdvisor";
    private static final String DEFAULT_LOCK_MANAGER = "lock-manager";
    private static final String LOCK_ARG ="lock-arg";

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String mode = element.getAttribute("mode");
        if ("aspectj".equals(mode)) {
            // mode="aspectj"
            //registerCacheAspect(element, parserContext);
            //TODO 未支持
        } else {
            // mode="proxy"
            configureAutoProxyCreator(element, parserContext);
        }
        return null;

    }

    public static void configureAutoProxyCreator(Element element, ParserContext parserContext) {
        AopNamespaceUtils.registerAutoProxyCreatorIfNecessary(parserContext, element);

        if (!parserContext.getRegistry().containsBeanDefinition(LOCK_ADVISOR_BEAN_NAME)) {
            Object eleSource = parserContext.extractSource(element);
            // Create the OperationSource definition.
            RootBeanDefinition sourceDef = new RootBeanDefinition(AnnotationLockOperationSource.class);
            sourceDef.setSource(eleSource);
            sourceDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            String sourceName = parserContext.getReaderContext().registerWithGeneratedName(sourceDef);

            // Create the Interceptor definition.
            RootBeanDefinition interceptorDef = new RootBeanDefinition(LockMethodIntercptor.class);
            interceptorDef.setSource(eleSource);
            interceptorDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            parseLockManagerProperty(element, interceptorDef);

            interceptorDef.getPropertyValues().add("operationSource", new RuntimeBeanReference(sourceName));
            parseLockArgProperty(element,interceptorDef);
            String interceptorName = parserContext.getReaderContext().registerWithGeneratedName(interceptorDef);

            // Create the LockAdvisor definition.
            RootBeanDefinition advisorDef = new RootBeanDefinition(OperationSourceAdvisor.class);
            advisorDef.setSource(eleSource);
            advisorDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            advisorDef.getPropertyValues().add("operationSource", new RuntimeBeanReference(sourceName));
            advisorDef.getPropertyValues().add("adviceBeanName", interceptorName);
            if (element.hasAttribute("order")) {
                advisorDef.getPropertyValues().add("order", element.getAttribute("order"));
            }
            parseLockArgProperty(element,advisorDef);
            parserContext.getRegistry().registerBeanDefinition(LOCK_ADVISOR_BEAN_NAME, advisorDef);

            CompositeComponentDefinition compositeDef = new CompositeComponentDefinition(element.getTagName(),
                    eleSource);
            compositeDef.addNestedComponent(new BeanComponentDefinition(sourceDef, sourceName));
            compositeDef.addNestedComponent(new BeanComponentDefinition(interceptorDef, interceptorName));
            compositeDef.addNestedComponent(new BeanComponentDefinition(advisorDef, LOCK_ADVISOR_BEAN_NAME));
            parserContext.registerComponent(compositeDef);
        }
    }

    private static void parseLockManagerProperty(Element element, BeanDefinition def) {
        if (element.hasAttribute(DEFAULT_LOCK_MANAGER)) {
            def.getPropertyValues().add("lockManager",
                    new RuntimeBeanReference(element.getAttribute(DEFAULT_LOCK_MANAGER)));
        }
    }

    private static void parseLockArgProperty(Element element, BeanDefinition def) {
        if (element.hasAttribute(LOCK_ARG)) {
            def.getPropertyValues().add("argumentsOperationSource",
                    new RuntimeBeanReference(element.getAttribute(LOCK_ARG)));
        }
    }
}
