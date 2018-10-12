package com.xlm.example.lock;

import com.xlm.example.aop.AnnotationOperationSource;
import com.xlm.example.aop.AnnotationParse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AnnotationLockOperationSource extends AnnotationOperationSource<List<LockSource>> {
    public AnnotationLockOperationSource() {
        List<AnnotationParse<List<LockSource>>> annotationParses = new ArrayList<>();
        annotationParses.add(new LockAnnotationParse());
        super.setAnnotationParses(annotationParses);
    }

}
