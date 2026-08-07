package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.util.Util;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public record TabPing(int latency, boolean isReal) implements TabEntry {
    public static final TabPing FAILURE = new TabPing(-1, false);

    public static final StyledString YOUR_PING_ = StyledString.fromString("Your ping:", CKColor.YELLOW.style);

    public static TabPing readFast(StyledStringReader source) {
        if(!source.skipIfNext(YOUR_PING_)) return FAILURE;
        source.skipSpace();

        int latency = Util.parseIntOr(source.readUntil("ms").toUnstyledString(), Integer.MIN_VALUE);
        if (latency == Integer.MIN_VALUE) return FAILURE;

        if (!source.skipIfNext("ms")) return FAILURE;

        return new TabPing(latency, true);
    }
}
