package com.xlm.example.lock;

import com.xlm.example.aop.AnnotationParse;
import org.springframework.beans.annotation.AnnotationBeanUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.AnnotatedElement;
import java.util.*;

public class LockAnnotationParse implements AnnotationParse<List<LockSource>> {
    @Override
    public List<LockSource> parse(AnnotatedElement element) {
        Set<Lock> locks = AnnotationUtils.getRepeatableAnnotations(element, Lock.class, Lock.LockList.class);
        if (locks == null||locks.isEmpty()) {
            return null;
        }
        ArrayList<LockSource> lockSources = new ArrayList<>(locks.size());
        for (Lock one : locks) {
            LockSource source = new LockSource();
            lockSources.add(source);
            AnnotationBeanUtils.copyPropertiesToBean(one, source);
        }
        Collections.sort(lockSources, Comparator.comparingInt(LockSource::getOrder));
        return lockSources;
    }
}
