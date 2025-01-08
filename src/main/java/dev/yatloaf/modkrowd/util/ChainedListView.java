package dev.yatloaf.modkrowd.util;

import java.util.AbstractList;
import java.util.List;

/**
 * Cheap concatenation of two lists for read access.
 *
 * @param <E> the type of elements in this list
 */
public class ChainedListView<E> extends AbstractList<E> {
    private final List<E> first;
    private final List<E> last;

    public ChainedListView(List<E> first, List<E> last) {
        this.first = first;
        this.last = last;
    }

    @Override
    public E get(int index) {
        int firstSize = this.first.size();
        return index < firstSize
                ? this.first.get(index)
                : this.last.get(index - firstSize);
    }

    @Override
    public int size() {
        return this.first.size() + this.last.size();
    }
}
