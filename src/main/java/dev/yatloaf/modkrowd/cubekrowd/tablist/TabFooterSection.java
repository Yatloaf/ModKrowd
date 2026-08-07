package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public record TabFooterSection(StyledString label, StyledString link, boolean isReal) {
    public static final TabFooterSection FAILURE = new TabFooterSection(StyledString.EMPTY, StyledString.EMPTY, false);

    public static TabFooterSection readFast(StyledStringReader source) {
        StyledString section = source.readUntil("|").strip();
        int lastSpaceIndex = section.lastIndexOf(" ");
        StyledString label = section.subView(0, lastSpaceIndex);
        StyledString link = section.subView(lastSpaceIndex + 1);
        if (label.isEmpty() || link.isEmpty()) return FAILURE;

        source.skipUntilAfter("|");

        return new TabFooterSection(label.isolate(), link.isolate(), true);
    }
}
