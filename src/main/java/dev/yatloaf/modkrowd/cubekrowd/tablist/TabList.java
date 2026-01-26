package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.util.text.StyledString;

public interface TabList {
    // TODO: Add TabEntry for this
    StyledString ___AND = StyledString.fromString("... and", CKColor.GRAY.style);

    TabEntry[] entries();
    TabEntry[] players();
    TabEntry self();
    boolean listsSubserver(Subserver subserver);
}
