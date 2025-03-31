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

        int latency = Util.parseIntOr(source.readUntil("ms").toUnstyledString(), -1);
        if (latency < 0) return FAILURE;

        source.skipUntilAfter("ms");

        return new TabPing(latency, true);
    }
}
