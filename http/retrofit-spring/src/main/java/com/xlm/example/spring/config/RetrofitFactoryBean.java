package com.xlm.example.spring.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import retrofit2.Retrofit;

public class RetrofitFactoryBean<T> implements FactoryBean<T>, InitializingBean {
    private Retrofit retrofit;
    private Class<T> interfaceClass;
    private String interfaceName;
    private T invoker;

    @Override
    public T getObject() throws Exception {
        return invoker;
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (interfaceClass == null) {
            interfaceClass = (Class<T>) Class.forName(interfaceName);
        }
        invoker = retrofit.create(interfaceClass);
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }
}
