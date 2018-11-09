package com.garfield.plugin.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public final class Lists {
    private Lists() {
    }

    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }

    @SafeVarargs
    public static <E> ArrayList<E> newArrayList(E... elements) {
        Objects.requireNonNull(elements); // for GWT
        int capacity = computeArrayListCapacity(elements.length);
        ArrayList<E> list = new ArrayList<E>(capacity);
        Collections.addAll(list, elements);
        return list;
    }


    private static int computeArrayListCapacity(int arraySize) {
        return (int) (5L + arraySize + (arraySize / 10));
    }
}