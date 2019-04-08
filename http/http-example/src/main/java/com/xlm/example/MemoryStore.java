package com.xlm.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class MemoryStore<T> implements Store<T> {
    LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<>();

    @Override
    public void store(List<T> storedList) {
        for (T stored : storedList) {
            store(stored);
        }
    }

    @Override
    public List<T> pullLast(int size) {
        List list = new ArrayList(size);
        list.add(pullLast());
        return list;
    }

    @Override
    public void store(T stored) {
        while (true) {
            try {
                queue.put(stored);
                return;
            } catch (Exception e) {

            }
        }

    }

    @Override
    public T pullLast() {
        while (true) {
            try {
                return queue.take();
            } catch (Exception e) {

            }
        }

    }
}
