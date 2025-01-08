package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.util.text.StyledStringReader;

import java.util.ArrayList;
import java.util.List;

public record TabFooter(TabFooterSection[] sections, boolean isReal) {

    public static TabFooter readSoft(StyledStringReader source) {
        List<TabFooterSection> sections = new ArrayList<>();
        boolean isReal = true;
        while (!source.isAtEnd()) {
            TabFooterSection section = TabFooterSection.readSoft(source);
            sections.add(section);
            isReal &= section.isReal();
        }

        return new TabFooter(sections.toArray(TabFooterSection[]::new), isReal);
    }
}
