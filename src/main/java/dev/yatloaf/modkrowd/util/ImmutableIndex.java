package dev.yatloaf.modkrowd.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.AbstractList;

/**
 * Immutable list with quick index operations, no duplicate items permitted
 *
 * @param <E> the type of elements in this list
 */
public class ImmutableIndex<E> extends AbstractList<E> {
    // Aren't these performance improvements negligible? (Optimization nihilism)

    private final E[] array;
    private final Object2IntMap<E> map;

    private ImmutableIndex(E[] array, Object2IntMap<E> map) {
        this.array = array;
        this.map = map;
    }

    @SafeVarargs
    public static <E> ImmutableIndex<E> ofStable(E... array) {
        Object2IntMap<E> map = new Object2IntOpenHashMap<>();
        for (int i = 0; i < array.length; i++) {
            if (map.containsKey(array[i])) {
                throw new IllegalArgumentException("No duplicates permitted!");
            }
            map.put(array[i], i);
        }
        return new ImmutableIndex<>(array, map);
    }

    @Override
    public int size() {
        return this.array.length;
    }

    @Override
    public E get(int index) {
        return this.array[index];
    }

    @Override
    public int indexOf(Object o) {
        return this.map.getOrDefault(o, -1);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.indexOf(o);
    }
}
