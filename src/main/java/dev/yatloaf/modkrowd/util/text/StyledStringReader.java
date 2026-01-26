package dev.yatloaf.modkrowd.util.text;

import java.util.Arrays;
import java.util.Map;

public class StyledStringReader {
    // TODO: More documentation

    private final int length;
    private final StyledString source;
    private int cursor;

    private StyledStringReader(StyledString source) {
        this.length = source.length();
        this.source = source;
        this.cursor = 0;
    }

    public static StyledStringReader of(StyledString source) {
        return new StyledStringReader(source);
    }

    public synchronized boolean isAtEnd() {
        return this.cursor == this.length;
    }

    /**
     * Reads {@code count} characters or until the end of the string and returns a subview.
     *
     * @param count Amount of characters to read if possible.
     * @return A subview of the string.
     */
    public synchronized StyledString read(int count) {
        int actualCount = Math.min(count, this.length - this.cursor);
        StyledString result = this.source.subView(this.cursor, this.cursor + actualCount);
        this.cursor += actualCount;
        return result;
    }

    public synchronized StyledString readAll() {
        StyledString result = this.source.subView(this.cursor);
        this.cursor = this.length;
        return result;
    }

    public synchronized StyledString readUntil(StyledString stop) {
        int stopIndex = this.source.subView(this.cursor).firstIndexOf(stop);
        return stopIndex == -1 ? this.readAll() : this.read(stopIndex);
    }

    public synchronized StyledString readUntil(String stop) {
        int stopIndex = this.source.subView(this.cursor).firstIndexOf(stop);
        return stopIndex == -1 ? this.readAll() : this.read(stopIndex);
    }

    public synchronized StyledString readUntilAny(String... stops) {
        int stopIndex = Arrays.stream(stops)
                .mapToInt(this.source.subView(this.cursor)::firstIndexOf)
                .filter(item -> item != -1)
                .reduce(Math::min)
                .orElse(this.length - this.cursor);
        return this.read(stopIndex);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public synchronized StyledString readWhile(String repeat) {
        int start = this.cursor;
        while (this.skipIfNext(repeat));
        return this.source.subView(start, this.cursor);
    }

    public synchronized StyledString peek(int count) {
        int actualCount = Math.min(count, this.length - this.cursor);
        return this.source.subView(this.cursor, this.cursor + actualCount);
    }

    public synchronized void skip(int count) {
        this.cursor += Math.min(count, this.length - this.cursor);
    }

    public synchronized void skipAll() {
        this.cursor = this.length;
    }

    public synchronized void skipUntil(StyledString stop) {
        int stopIndex = this.source.subView(this.cursor).firstIndexOf(stop);
        if (stopIndex == -1) {
            this.skipAll();
        } else {
            this.skip(stopIndex);
        }
    }

    public synchronized void skipUntilAfter(StyledString stop) {
        int stopIndex = this.source.subView(this.cursor).firstIndexOf(stop);
        if (stopIndex == -1) {
            this.skipAll();
        } else {
            this.skip(stopIndex + stop.length());
        }
    }

    public synchronized void skipUntilAfter(String stop) {
        int[] stopCodePoints = stop.codePoints().toArray();
        int stopIndex = this.source.subView(this.cursor).firstIndexOfCodePoints(stopCodePoints);
        if (stopIndex == -1) {
            this.skipAll();
        } else {
            this.skip(stopIndex + stopCodePoints.length);
        }
    }

    public synchronized boolean skipIfNext(StyledString next) {
        if (this.source.subView(this.cursor).startsWith(next)) {
            this.cursor += next.length();
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean skipIfNext(String next) {
        int[] nextCodePoints = next.codePoints().toArray();
        if (this.source.subView(this.cursor).startsWithCodePoints(nextCodePoints)) {
            this.cursor += nextCodePoints.length;
            return true;
        } else {
            return false;
        }
    }

    public synchronized <V> V mapNextOrDefault(Map<StyledString, V> map, V fallback) {
        int keyMaxLength = map.keySet().stream().mapToInt(StyledString::length).max().orElse(0);
        int maxLength = Math.min(keyMaxLength, this.length - this.cursor);

        // TODO: Rabin-Karp :3
        for (int index = 0; index <= maxLength; index++) {
            StyledString sub = this.source.subView(this.cursor, this.cursor + index);
            if (map.containsKey(sub)) {
                this.cursor += index;
                return map.get(sub);
            }
        }
        return fallback;
    }
}
