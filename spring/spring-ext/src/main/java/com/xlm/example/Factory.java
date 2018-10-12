package com.xlm.example;

import java.util.concurrent.locks.Lock;

public interface Factory<T,R> {
    T create(R name);
}
