package dev.yatloaf.modkrowd.cubekrowd.common;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.util.RabinKarp;
import dev.yatloaf.modkrowd.util.text.StyledString;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.List;

public final class CKStuff {
    @SuppressWarnings("UnnecessaryUnicodeEscape")
    public static final String RIGHT_ARROW = "\u279B";
    public static final Int2ObjectMap<Int2ObjectMap<Object>> BAD_WORD_HASHES = new Int2ObjectOpenHashMap<>();
    public static final String CUBEKROWD_IP = "167.235.185.144";
    public static final String CUBEKROWD_ADDRESS = "cubekrowd.net"; // server address containing this is treated as CubeKrowd (not case-sensitive)
    public static final String SUBSERVER_COMMAND = "whereami"; // command that returns subserver

    // TODO: Complete the list
    static {
        Int2ObjectMap<Object> length4 = new Int2ObjectOpenHashMap<>();
        length4.put(8223648, ModKrowd.USELESS);
        length4.put(1736401, ModKrowd.USELESS);
        length4.put(14513687, ModKrowd.USELESS);
        length4.put(8010194, ModKrowd.USELESS);
        BAD_WORD_HASHES.put(4, length4);
        Int2ObjectMap<Object> length6 = new Int2ObjectOpenHashMap<>();
        BAD_WORD_HASHES.put(6, length6);
    }

    public static boolean addressIsCubeKrowd(String address) {
        return address.contains(CUBEKROWD_IP) || address.toLowerCase().contains(CUBEKROWD_ADDRESS);
    }

    public static TextCache censor(TextCache value) {
        StyledString result = value.styledString();
        for (Int2ObjectMap.Entry<Int2ObjectMap<Object>> entry : BAD_WORD_HASHES.int2ObjectEntrySet()) {
            int length = entry.getIntKey();
            Int2ObjectMap<Object> map = entry.getValue();
            List<RabinKarp.Result<Object>> finds = RabinKarp.mapHashesAscii(result, map, length);
            String replacement = "*".repeat(length);
            for (RabinKarp.Result<Object> f : finds) {
                result = result.insert(f.index(), replacement);
            }
        }
        return TextCache.of(result);
    }
}
