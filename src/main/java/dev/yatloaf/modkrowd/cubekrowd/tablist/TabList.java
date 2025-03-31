package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
import dev.yatloaf.modkrowd.util.text.StyledString;

public interface TabList {
    StyledString ___AND = StyledString.fromString("... and", CKColor.GRAY.style);

    TabEntryCache[] entries();
    TabEntryCache[] players();
    TabEntryCache self();
    boolean listsSubserver(Subserver subserver);
    boolean isLoaded();
}
