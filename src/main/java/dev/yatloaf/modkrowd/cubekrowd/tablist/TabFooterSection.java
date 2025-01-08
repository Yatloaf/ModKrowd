package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public record TabFooterSection(StyledString label, StyledString link, boolean isReal) {

    public static TabFooterSection readSoft(StyledStringReader source) {
        StyledString section = source.readUntil("|").strip();
        int lastSpaceIndex = section.lastIndexOf(" ");
        StyledString label = section.subView(0, lastSpaceIndex).isolate();
        StyledString link = section.subView(lastSpaceIndex + 1).isolate();

        source.skipUntilAfter("|");

        boolean isReal = !label.isEmpty() && !link.isEmpty();
        return new TabFooterSection(label, link, isReal);
    }
}
