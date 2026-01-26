package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.util.text.StyledString;

public enum TabLiteral implements TabEntry {
    EMPTY(StyledString.EMPTY),
    SERVERS(StyledString.fromString("Servers", CKColor.GOLD.style.withBold(true))),
    UNKNOWN(StyledString.fromString("?"));

    public final StyledString text;

    TabLiteral(StyledString text) {
        this.text = text;
    }

    public TabLiteral parseSpecific(StyledString source) {
        return source.equals(this.text) ? this : UNKNOWN;
    }

    public boolean isReal() {
        return this != UNKNOWN;
    }
}
