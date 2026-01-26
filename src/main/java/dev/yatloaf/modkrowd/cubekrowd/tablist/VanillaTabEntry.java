package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.util.text.StyledString;

public record VanillaTabEntry(TextCache name, Subserver subserver) implements TabEntry {
    @Override
    public StyledString playerName() {
        return this.name.styledString();
    }

    @Override
    public Subserver playerSubserver() {
        return this.subserver;
    }
}
