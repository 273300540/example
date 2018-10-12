package com.xlm.example.aop;

import java.lang.reflect.AnnotatedElement;

public interface AnnotationParse<T> {
    T  parse(AnnotatedElement element);
}
