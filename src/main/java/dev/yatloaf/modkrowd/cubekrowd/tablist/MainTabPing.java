package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;
import dev.yatloaf.modkrowd.util.Util;

public record MainTabPing(int latency, boolean isReal) implements TabEntry {
    public static final MainTabPing FAILURE = new MainTabPing(-1, false);

    public static final StyledString YOUR_PING_ = StyledString.fromString("Your ping:", CKColor.YELLOW.style);

    public static MainTabPing readFast(StyledStringReader source) {
        source.skipUntilAfter(YOUR_PING_);
        source.skipUntilAfter(" ");

        String latencyString = source.readUntil("ms").toUnstyledString();
        if (latencyString.isEmpty()) return FAILURE;

        int latency = Util.parseIntOr(latencyString, -1);
        if (latency == -1) return FAILURE;

        source.skipUntilAfter("ms");

        return new MainTabPing(latency, true);
    }
}
