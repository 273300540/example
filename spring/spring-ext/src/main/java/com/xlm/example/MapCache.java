package com.xlm.example;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class MapCache<K, V> {
    public static final Object EMPTY_OBJECT = new Object();
    private ConcurrentHashMap<K, V> concurrentHashMap;
    private V emptyObject;

    public MapCache(V emptyObject) {
        this.emptyObject = emptyObject;
        concurrentHashMap  = new ConcurrentHashMap(16);
    }

    public V getValue(K key, Function<K, V> function) {
        V object = concurrentHashMap.computeIfAbsent(key, one -> {
            V result = function.apply(key);
            return result == null ? emptyObject : result;
        });
        return object == emptyObject ? null : object;
    }
}
