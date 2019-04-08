package com.xlm.example;

import java.util.List;
import java.util.concurrent.TimeUnit;
/**存储器*/
public interface Store<T> {
    void store(List<T> storedList);

    List<T> pullLast(int size);

    void store(T stored);

    T pullLast();
}
