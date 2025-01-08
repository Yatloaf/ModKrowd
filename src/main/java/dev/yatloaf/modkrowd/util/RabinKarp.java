package dev.yatloaf.modkrowd.util;

import dev.yatloaf.modkrowd.util.text.StyledString;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;

public final class RabinKarp {
    // <https://www.geeksforgeeks.org/java-program-for-rabin-karp-algorithm-for-pattern-searching/>
    // That doesn't account for overflow correctly, so a few '% prime' are added here
    // Maybe a few ints should be upgraded to longs

    private static final int ASCII_BASE = 128;
    private static final int ASCII_PRIME = 16777127; // Low enough to avoid overflow

    public static int findHashAscii(StyledString haystack, int needleHash, int needleLength) {
        return findHash(haystack, needleHash, needleLength, ASCII_BASE, ASCII_PRIME);
    }

    public static int findHash(StyledString haystack, int needleHash, int needleLength, int base, int prime) {
        int haystackLength = haystack.length();
        int rollingHash = hash(haystack.subView(0, needleLength), base, prime);

        int mask = powMod(base, needleLength - 1, prime);

        for (int index = 0; index + needleLength <= haystackLength; index++) {
            if (needleHash == rollingHash) {
                return index;
            }

            if (index + needleLength < haystackLength) {
                rollingHash = rollHash(rollingHash, haystack.codePointAt(index), haystack.codePointAt(index + needleLength),
                        mask, base, prime);
            }
        }
        return -1;
    }

    public static <V> List<Result<V>> mapHashesAscii(StyledString haystack, Int2ObjectMap<V> needleHashes, int needleLength) {
        return mapHashes(haystack, needleHashes, needleLength, ASCII_BASE, ASCII_PRIME);
    }

    public static <V> List<Result<V>> mapHashes(StyledString haystack, Int2ObjectMap<V> needleHashes, int needleLength, int base, int prime) {
        int haystackLength = haystack.length();
        if (haystackLength < needleLength) return List.of();
        int rollingHash = hash(haystack.subView(0, needleLength), base, prime);

        List<Result<V>> results = new ArrayList<>();

        int mask = powMod(base, needleLength - 1, prime);

        for (int index = 0; index + needleLength <= haystackLength; index++) {
            if (needleHashes.containsKey(rollingHash)) {
                results.add(new Result<>(index, needleHashes.get(rollingHash)));
            }

            if (index + needleLength < haystackLength) {
                rollingHash = rollHash(rollingHash, haystack.codePointAt(index), haystack.codePointAt(index + needleLength),
                        mask, base, prime);

                if (rollingHash < 0) {
                    rollingHash += prime;
                }
            }
        }
        return results;
    }

    public record Result<T>(int index, T t) {}

    public static int hashAscii(String value) {
        return hash(value, ASCII_BASE, ASCII_PRIME);
    }

    public static int hash(String value, int base, int prime) {
        int result = 0;
        PrimitiveIterator.OfInt iterator = value.codePoints().iterator();
        while (iterator.hasNext()) {
            result = (base * result + iterator.nextInt()) % prime;
        }
        return result;
    }

    public static int hashAscii(StyledString value) {
        return hash(value, ASCII_BASE, ASCII_PRIME);
    }

    public static int hash(StyledString value, int base, int prime) {
        int result = 0;
        for (int i = 0; i < value.length(); i++) {
            result = (base * result + value.codePointAt(i)) % prime;
        }
        return result;
    }

    public static int rollHash(int source, int tail, int head, int mask, int base, int prime) {
        return ((source + prime /* avoid underflow */ - tail * mask % prime) % prime /* avoid overflow */
                * base + head) % prime;
    }

    private static int powMod(int base, int exponent, int modulo) {
        int result = 1;
        for (int i = 0; i < exponent; i++) {
            result = (base * result) % modulo;
        }
        return result;
    }
}
