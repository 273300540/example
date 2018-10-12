package com.xlm.example.plugin;

import org.apache.ibatis.executor.BatchExecutor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Intercepts(value = {@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class BatchPlugin implements Interceptor {
    BatchExecutor batchExecutor;

    public Object intercept(Invocation invocation) throws Throwable {
        if (batchExecutor == null) {
            return invocation.proceed();
        }
        Object args = invocation.getArgs()[1];
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        List<BatchResult> batchResults = null;
        Object[] argsArray = (Object[])((Map) args).get("array");

        Map map = new HashMap();
        int i = 0;
        for (Object one : argsArray) {
            map.put(String.valueOf(i), one);
            map.put("param" + (i + 1), one);
            batchExecutor.update(mappedStatement, map);
        }
        batchResults = batchExecutor.flushStatements();

      /*  String className = mappedStatement.getId().substring(0, mappedStatement.getId().lastIndexOf("."));
        String method = mappedStatement.getId().substring(className.length() + 1);
        Class clazz = Class.forName(className);
        Class<?>[] paramArray = new Class[invocation.getArgs().length];
        i = 0;
        for (Object one : invocation.getArgs()) {
            paramArray[i] = one.getClass();
            i++;
        }
        Method me = clazz.getDeclaredMethod(className, paramArray);
        me.getReturnType()*/;
        return 0;
    }

    public Object plugin(Object target) {
        if (target instanceof BatchExecutor) {
            batchExecutor = (BatchExecutor) target;
        }
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {

    }
}
