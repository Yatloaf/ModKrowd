package dev.yatloaf.modkrowd.cubekrowd.tablist.cache;

import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.ThemedCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabFooter;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public class TabFooterCache extends ThemedCache {
    private TabFooter tabFooter;

    public TabFooterCache(TextCache original) {
        super(original);
    }

    public TabFooter tabFooterSoft() {
        if (this.tabFooter == null) {
            this.tabFooter = TabFooter.readSoft(StyledStringReader.of(this.original.styledString()));
        }
        return this.tabFooter;
    }
}
