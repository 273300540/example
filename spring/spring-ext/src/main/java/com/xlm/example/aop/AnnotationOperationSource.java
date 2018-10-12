package com.xlm.example.aop;

import com.xlm.example.MapCache;
import com.xlm.example.ObjectsKey;
import com.xlm.example.lock.LockSource;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class AnnotationOperationSource<T> implements OperationSource<T> {
    //private ConcurrentHashMap<ObjectsKey, Object> concurrentHashMap = new ConcurrentHashMap<>();
    private MapCache<ObjectsKey, Object> mapCache = new MapCache<>(MapCache.EMPTY_OBJECT);
    private List<AnnotationParse<T>> annotationParses;

    @Override
    public T getOperationSources(Method method, Class targetClass) {
        ObjectsKey key = new ObjectsKey(method, targetClass);
        Object exists = mapCache.getValue(key, oneKey -> {

            Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
            // If we are dealing with method with generic parameters, find the original method.
            specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
            for (AnnotationParse<T> one : annotationParses) {
                T sources = one.parse(specificMethod);
                if (sources != null) {
                    return sources;
                }
            }
            return null;
        });
        return (T) exists;
    }

    public List<AnnotationParse<T>> getAnnotationParses() {
        return annotationParses;
    }

    public void setAnnotationParses(List<AnnotationParse<T>> annotationParses) {
        this.annotationParses = annotationParses;
    }
}
