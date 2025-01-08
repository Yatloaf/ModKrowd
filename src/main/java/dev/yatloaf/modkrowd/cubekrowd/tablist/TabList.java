package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
import dev.yatloaf.modkrowd.util.text.StyledString;

public interface TabList {
    StyledString ___AND = StyledString.fromString("... and", CKColor.GRAY.style);
    StyledString ARCKADE = StyledString.concat(
            StyledString.fromString("ar", CKColor.DARK_PURPLE.style.withBold(true)),
            StyledString.fromString("CK", CKColor.GOLD.style.withBold(true)),
            StyledString.fromString("ade", CKColor.DARK_PURPLE.style.withBold(true))
    );

    TabEntryCache[] entries();
    TabEntryCache[] players();
    TabEntryCache self();
    boolean listsSubserver(Subserver subserver);
    boolean isLoaded();
}
