package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.util.text.StyledStringReader;

import java.util.ArrayList;
import java.util.List;

public record TabFooter(TabFooterSection[] sections, boolean isReal) {
    public static final TabFooter FAILURE = new TabFooter(new TabFooterSection[0], false);

    public static TabFooter readFast(StyledStringReader source) {
        List<TabFooterSection> sections = new ArrayList<>();
        while (!source.isAtEnd()) {
            TabFooterSection section = TabFooterSection.readFast(source);
            if (!section.isReal()) return FAILURE;
            sections.add(section);
        }
        if (sections.isEmpty()) return FAILURE;

        return new TabFooter(sections.toArray(TabFooterSection[]::new), true);
    }
}
