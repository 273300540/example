package com.xlm.example.lock;

import java.util.concurrent.TimeUnit;
public class LockSource {
    private String name;
    private String factoryName;
    private int order;
    private int timeOut;
    private TimeUnit timeUnit=TimeUnit.SECONDS;
    private boolean expression;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public boolean isExpression() {
        return expression;
    }

    public void setExpression(boolean expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "LockSource{" +
                "name=" + name  +
                ", factoryName=" + factoryName  +
                ", order=" + order +
                ", timeOut=" + timeOut +
                ", timeUnit=" + timeUnit +
                ", expression=" + expression +
                '}';
    }
}
