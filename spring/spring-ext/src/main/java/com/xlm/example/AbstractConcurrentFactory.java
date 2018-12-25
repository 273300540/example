package com.xlm.example;

import java.util.Optional;

public abstract class AbstractConcurrentFactory<T, R> implements Factory<T, R> {
    private MapCache<R, Optional<T>> mapCache = new MapCache<>(Optional.empty(),false);


    @Override
    public T create(R name) {
        Optional<T> optional = mapCache.getValue(name, one -> {
            T instance = newInstance(one);
            return Optional.of(instance);
        });
        return optional == null ? null : optional.get();
    }

    public abstract T newInstance(R name);
}
