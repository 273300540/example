package com.xlm.example.spel;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;

public class MethodExpressionRootObject {


    private final Method method;

    private final Object[] args;

    private final Object target;

    private final Class<?> targetClass;


    public MethodExpressionRootObject(
            Method method, Class<?> targetClass,Object[] args, Object target) {

        this(method,targetClass,args,target,false);
    }
    public MethodExpressionRootObject(
            Method method, Class<?> targetClass,Object[] args, Object target,boolean extract) {

        Assert.notNull(method, "Method is required");
        Assert.notNull(targetClass, "targetClass is required");
        this.method = method;
        this.target = target;
        this.targetClass = targetClass;
        if(extract) {
            this.args = extractArgs(method, args);
        }else {
            this.args = args;
        }
    }
    private Object[] extractArgs(Method method, Object[] args) {
        if (!method.isVarArgs()) {
            return args;
        }
        Object[] varArgs = ObjectUtils.toObjectArray(args[args.length - 1]);
        Object[] combinedArgs = new Object[args.length - 1 + varArgs.length];
        System.arraycopy(args, 0, combinedArgs, 0, args.length - 1);
        System.arraycopy(varArgs, 0, combinedArgs, args.length - 1, varArgs.length);
        return combinedArgs;
    }

    public Method getMethod() {
        return this.method;
    }

    public String getMethodName() {
        return this.method.getName();
    }

    public Object[] getArgs() {
        return this.args;
    }

    public Object getTarget() {
        return this.target;
    }

    public Class<?> getTargetClass() {
        return this.targetClass;
    }
}
