package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.util.text.StyledString;

public enum GameTabLabel implements TabEntry {
    EMPTY(StyledString.EMPTY),
    WELCOME_TO(StyledString.fromString("Welcome to", CKColor.DARK_AQUA.style.withBold(true))),
    ARCKADE(StyledString.concat(
            StyledString.fromString("ar", CKColor.DARK_PURPLE.style.withBold(true)),
            StyledString.fromString("CK", CKColor.GOLD.style.withBold(true)),
            StyledString.fromString("ade", CKColor.DARK_PURPLE.style.withBold(true))
    )),
    SERVER(StyledString.fromString("Server", CKColor.GOLD.style.withBold(true))),
    MODE(StyledString.fromString("Mode", CKColor.GOLD.style.withBold(true))),
    PLAYERS(StyledString.fromString("Players", CKColor.GOLD.style.withBold(true))),
    STATUS(StyledString.fromString("Status", CKColor.GOLD.style.withBold(true))),
    YOUR_GAME(StyledString.fromString("Your game", CKColor.GOLD.style.withBold(true))),
    UNDER(StyledString.fromString("Under", CKColor.RED.style.withBold(true))),
    MAINTENANCE(StyledString.fromString("Maintenance", CKColor.RED.style.withBold(true))),
    UNKNOWN(StyledString.fromString("?"));

    public final StyledString text;

    GameTabLabel(StyledString text) {
        this.text = text;
    }

    public GameTabLabel parseSpecific(StyledString source) {
        return source.equals(this.text) ? this : UNKNOWN;
    }

    public boolean matches(StyledString source) {
        return source.equals(this.text);
    }
}
