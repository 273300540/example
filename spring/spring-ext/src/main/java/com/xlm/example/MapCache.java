package com.xlm.example;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class MapCache<K, V> {
    public static final Object EMPTY_OBJECT = new Object();
    private ConcurrentHashMap<K, V> concurrentHashMap;
    private V emptyObject;
    private boolean emptyObjectReturnNull;

    public MapCache(V emptyObject) {
        this(emptyObject, true);
    }
    public MapCache() {
        this(null);
    }

    public MapCache(V emptyObject, boolean emptyObjectReturnNull) {
        this.emptyObject = emptyObject;
        concurrentHashMap = new ConcurrentHashMap(16);
        this.emptyObjectReturnNull = emptyObjectReturnNull;
    }

    public V getValue(K key, Function<K, V> function) {
        V object = concurrentHashMap.computeIfAbsent(key, one -> {
            V result = function.apply(key);
            return result == null ? emptyObject : result;
        });
        return emptyObjectReturnNull ? (object == emptyObject ? null : object) : object;
    }

    public V getValue(K key) {
        V object = concurrentHashMap.get(key);
        return emptyObjectReturnNull ? (object == emptyObject ? null : object) : object;
    }
}
