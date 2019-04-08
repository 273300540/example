package com.xlm.example;

public class GenericPool<T> implements Pool<T>  {
    @Override
    public T borrow() {
        return null;
    }

    @Override
    public void back(T pooled) {

    }

    @Override
    public void dead(T pooled) {

    }
}
