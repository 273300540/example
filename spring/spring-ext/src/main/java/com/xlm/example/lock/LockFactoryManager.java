package com.xlm.example.lock;

import com.xlm.example.Factory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockFactoryManager implements Factory<Factory<Lock,String>, String> {
    private ConcurrentHashMap<String, Factory<Lock, String>> map = new ConcurrentHashMap<>();

    @Override
    public Factory create(String name) {
        return map.computeIfAbsent(name, one -> new LockFactory());
    }

    public static class LockFactory implements Factory<Lock, String> {
        private ConcurrentHashMap<String, Lock> map = new ConcurrentHashMap<>();

        @Override
        public Lock create(String name) {
            return map.computeIfAbsent(name, one -> new ReentrantLock());
        }
    }
}
