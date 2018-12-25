package com.xlm.example.lock;

import com.xlm.example.AbstractConcurrentFactory;
import com.xlm.example.Factory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockFactoryManager extends AbstractConcurrentFactory<Factory<Lock, String>, String> {

    @Override
    public Factory<Lock, String> newInstance(String name) {
        return new LockFactory();
    }

    public static class LockFactory extends AbstractConcurrentFactory<Lock, String> {
        @Override
        public Lock newInstance(String name) {
            return new ReentrantLock();
        }
    }
}
