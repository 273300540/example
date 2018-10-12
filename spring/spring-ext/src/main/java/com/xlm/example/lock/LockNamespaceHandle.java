package com.xlm.example.lock;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class LockNamespaceHandle extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("annotation-driven", new LockAnnotationDriverParse());
    }
}
