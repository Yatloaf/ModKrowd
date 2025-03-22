package dev.yatloaf.modkrowd.cubekrowd.common;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.util.RabinKarp;
import dev.yatloaf.modkrowd.util.text.StyledString;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import java.util.List;

public final class CKStuff {
    @SuppressWarnings("UnnecessaryUnicodeEscape")
    public static final String RIGHT_ARROW = "\u279B";
    public static final RabinKarp.Needles<Object> BAD_WORD_HASHES = new RabinKarp.Needles<>();
    public static final String CUBEKROWD_IP = "167.235.185.144";
    public static final String CUBEKROWD_ADDRESS = "cubekrowd.net"; // server address containing this is treated as CubeKrowd (not case-sensitive)
    public static final String SUBSERVER_COMMAND = "whereami"; // command that returns subserver

    // All known words
    static {
        BAD_WORD_HASHES.put(4, 8223648, ModKrowd.USELESS);
        BAD_WORD_HASHES.put(4, 1736401, ModKrowd.USELESS);
        BAD_WORD_HASHES.put(4, 14513687, ModKrowd.USELESS);
        BAD_WORD_HASHES.put(4, 8010194, ModKrowd.USELESS);
        BAD_WORD_HASHES.put(5, 3955782, ModKrowd.USELESS);
        BAD_WORD_HASHES.put(5, 12545495, ModKrowd.USELESS);
        BAD_WORD_HASHES.put(5, 4151181, ModKrowd.USELESS);
        BAD_WORD_HASHES.put(5, 12561961, ModKrowd.USELESS);
        BAD_WORD_HASHES.put(5, 6088137, ModKrowd.USELESS);
        BAD_WORD_HASHES.put(6, 3026912, ModKrowd.USELESS);
        BAD_WORD_HASHES.put(6, 14139499, ModKrowd.USELESS);
        BAD_WORD_HASHES.put(6, 12714393, ModKrowd.USELESS);
        BAD_WORD_HASHES.put(6, 14397938, ModKrowd.USELESS);
        BAD_WORD_HASHES.put(6, 2624441, ModKrowd.USELESS);
        BAD_WORD_HASHES.put(7, 15791507, ModKrowd.USELESS);
        BAD_WORD_HASHES.put(7, 16575562, ModKrowd.USELESS);
        BAD_WORD_HASHES.put(7, 8555973, ModKrowd.USELESS);
        BAD_WORD_HASHES.put(7, 4358626, ModKrowd.USELESS);
        BAD_WORD_HASHES.put(8, 10197496, ModKrowd.USELESS);
    }

    public static boolean addressIsCubeKrowd(String address) {
        return address.contains(CUBEKROWD_IP) || address.toLowerCase().contains(CUBEKROWD_ADDRESS);
    }

    public static TextCache censor(TextCache value) {
        StyledString result = value.styledString();
        StyledString source = result.mapCodePoints(Character::toLowerCase); // Search ignoring case
        for (RabinKarp.Needle<Object> needle : BAD_WORD_HASHES) {
            int length = needle.length();
            Int2ObjectMap<Object> map = needle.map();
            List<RabinKarp.Result<Object>> finds = RabinKarp.mapHashesAscii(source, map, length);
            String replacement = "*".repeat(length);
            for (RabinKarp.Result<Object> f : finds) {
                result = result.insert(f.index(), replacement);
            }
        }
        return TextCache.of(result);
    }
}
