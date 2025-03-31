package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;
import dev.yatloaf.modkrowd.util.Util;

public record TabPing(int latency, boolean isReal) implements TabEntry {
    public static final TabPing FAILURE = new TabPing(-1, false);

    public static final StyledString YOUR_PING_ = StyledString.fromString("Your ping:", CKColor.YELLOW.style);

    public static TabPing readFast(StyledStringReader source) {
        source.skipUntilAfter(YOUR_PING_);
        source.skipUntilAfter(" ");

        String latencyString = source.readUntil("ms").toUnstyledString();
        if (latencyString.isEmpty()) return FAILURE;

        int latency = Util.parseIntOr(latencyString, -1);
        if (latency == -1) return FAILURE;

        source.skipUntilAfter("ms");

        return new TabPing(latency, true);
    }
}
