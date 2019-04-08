package com.xlm.example;

public interface Pool<T> {
    T borrow();
    void back(T pooled);
    void dead(T pooled);
}
