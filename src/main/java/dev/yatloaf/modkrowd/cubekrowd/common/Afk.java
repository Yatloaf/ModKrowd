package dev.yatloaf.modkrowd.cubekrowd.common;

import dev.yatloaf.modkrowd.util.Util;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

import java.util.Map;

public enum Afk {
    FALSE(StyledString.EMPTY, StyledString.fromString("is no longer AFK", CKColor.DARK_PURPLE.style)),
    TRUE(StyledString.fromString("*", CKColor.DARK_PURPLE.style), StyledString.fromString("is now AFK", CKColor.DARK_PURPLE.style)),
    UNKNOWN(StyledString.EMPTY, StyledString.fromString("?"));

    public final StyledString star;
    public final StyledString messageSuffix;

    Afk(StyledString star, StyledString messageSuffix) {
        this.star = star;
        this.messageSuffix = messageSuffix;
    }

    public static final Map<StyledString, Afk> FROM_SUFFIX = Util.arrayToMap(values(), item -> item.messageSuffix, item -> item);

    public static Afk read(StyledStringReader source) {
        if (source.skipIfNext(TRUE.star)) {
            return TRUE;
        } else {
            return FALSE;
        }
    }

    public static Afk readSuffix(StyledStringReader source) {
        return source.mapNextOrDefault(FROM_SUFFIX, UNKNOWN);
    }

    public boolean isReal() {
        return this != UNKNOWN;
    }
}
