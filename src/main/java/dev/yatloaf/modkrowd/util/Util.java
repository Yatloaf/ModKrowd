package dev.yatloaf.modkrowd.util;

import it.unimi.dsi.fastutil.chars.Char2CharMap;
import it.unimi.dsi.fastutil.chars.Char2CharOpenHashMap;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.VersionParsingException;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

@SuppressWarnings("UnnecessaryUnicodeEscape")
public final class Util {
    private static final Char2CharMap NUMERAL_TO_SUPERSCRIPT = new Char2CharOpenHashMap();
    static {
        NUMERAL_TO_SUPERSCRIPT.put('0', '⁰');
        NUMERAL_TO_SUPERSCRIPT.put('1', '¹');
        NUMERAL_TO_SUPERSCRIPT.put('2', '²');
        NUMERAL_TO_SUPERSCRIPT.put('3', '³');
        NUMERAL_TO_SUPERSCRIPT.put('4', '⁴');
        NUMERAL_TO_SUPERSCRIPT.put('5', '⁵');
        NUMERAL_TO_SUPERSCRIPT.put('6', '⁶');
        NUMERAL_TO_SUPERSCRIPT.put('7', '⁷');
        NUMERAL_TO_SUPERSCRIPT.put('8', '⁸');
        NUMERAL_TO_SUPERSCRIPT.put('9', '⁹');
        NUMERAL_TO_SUPERSCRIPT.put('-', '\u207B');
        NUMERAL_TO_SUPERSCRIPT.put('k', '\u1d4f');
    }

    public static String superscript(int n, boolean abbreviate) {
        char[] chars = abbreviate && n >= 1000
                ? String.valueOf(n / 1000).concat("k").toCharArray()
                : String.valueOf(n).toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] = NUMERAL_TO_SUPERSCRIPT.get(chars[i]);
        }
        return String.valueOf(chars);
    }

    public static String normalizeIntOr(String s, String fallback) {
        try {
            return Integer.toString(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    public static int parseIntOr(String s, int fallback) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    @Deprecated(forRemoval = true, since = "0.3.0")
    public static <T, R> R[] listToArray(List<T> list, Function<T, R> mapper, IntFunction<R[]> generator) {
        return list.stream().map(mapper).toArray(generator);
    }

    public static <T, K, V> Map<K, V> arrayToMap(T[] arr, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return Arrays.stream(arr).collect(Collectors.toMap(keyMapper, valueMapper));
    }

    @Deprecated(forRemoval = true, since = "0.1.1")
    public static SemanticVersion constVersion(String s) {
        try {
            return SemanticVersion.parse(s);
        } catch (VersionParsingException e) {
            throw new AssertionError(e);
        }
    }

    public static void sendCommandPacket(ClientPacketListener listener, String command) {
        // Fabric API mixes in listener.sendCommand() in a way that throws sometimes, so this is used instead
        listener.send(new ServerboundChatCommandPacket(command));
    }
}
