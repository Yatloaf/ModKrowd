package dev.yatloaf.modkrowd.cubekrowd.tablist.cache;

import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.ThemedCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabHeader;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public class TabHeaderCache extends ThemedCache {
    private TabHeader tabHeader;

    public TabHeaderCache(TextCache original) {
        super(original);
    }

    public TabHeader tabHeaderSoft() {
        if (this.tabHeader == null) {
            this.tabHeader = TabHeader.readSoft(StyledStringReader.of(this.original.styledString()));
        }
        return this.tabHeader;
    }
}
