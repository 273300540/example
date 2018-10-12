package com.xlm.example.lock;

import com.xlm.example.Factory;
import com.xlm.example.MapCache;
import com.xlm.example.ObjectsKey;
import com.xlm.example.aop.ArgumentsOperationSource;
import com.xlm.example.spel.MethodExpressionRootObject;
import org.springframework.beans.BeanUtils;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class ArgumentsOperationSourceImpl implements ArgumentsOperationSource<List<LockSource>> {
    MapCache<ObjectsKey, Object> mapCache = new MapCache<>(MapCache.EMPTY_OBJECT);

    @Override
    public List<LockSource> getOperationSources(Method method, Class targetClass, Object target, Object[] args) {

        Lock.LockSupplier supplier = getSupplier(method, targetClass);
        if (supplier == null) {
            return null;
        }
        Factory<List<LockSource>, MethodExpressionRootObject> methodExpressionRootObjectFactory = BeanUtils.instantiateClass(supplier.value());
        return methodExpressionRootObjectFactory.create(new MethodExpressionRootObject(method, targetClass, args, target, false));
    }

    @Override
    public boolean match(Method method, Class targetClass) {
        return getSupplier(method, targetClass) != null;
    }

    private Lock.LockSupplier getSupplier(Method method, Class targetClass) {
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        // If we are dealing with method with generic parameters, find the original method.
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        Method finalMethod = specificMethod;
        return (Lock.LockSupplier) mapCache.getValue(ObjectsKey.valueOf(specificMethod, targetClass), oneKey -> {

            return AnnotationUtils.getAnnotation(finalMethod, Lock.LockSupplier.class);
        });
    }
}
