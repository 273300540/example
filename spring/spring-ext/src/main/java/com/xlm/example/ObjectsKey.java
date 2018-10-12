package com.xlm.example;


import java.util.Arrays;

public class ObjectsKey {
    private Object[] objects;

    public static ObjectsKey valueOf(Object... objects) {
        return new ObjectsKey(objects);
    }

    public ObjectsKey(Object... objects) {
        this.objects = objects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ObjectsKey)) return false;
        ObjectsKey that = (ObjectsKey) o;
        return Arrays.equals(objects, that.objects);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(objects);
    }
}
