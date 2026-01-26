package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.util.Util;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public record GameTabSubserver(Subserver subserver, StyledString subserverName, MinigameMode mode, StyledString spaces,
                               int playerCount, int playerLimit, boolean isReal) implements TabEntry {
    public static final GameTabSubserver FAILURE = new GameTabSubserver(
            Subservers.UNKNOWN, StyledString.EMPTY, MinigameMode.UNKNOWN, StyledString.EMPTY,
            -1, -1, false
    );

    public static final int OFFLINE_SHADOW = 0x3F_AA_00_00;
    public static final StyledString OFFLINE = StyledString.fromString("Offline", CKColor.SLEET.style.withItalic(true).withShadowColor(OFFLINE_SHADOW));

    public static GameTabSubserver readFast(StyledStringReader source) {
        StyledString subserverName = source.readUntilAny(":", " (");
        Subserver subserver = Subservers.fromTabName(subserverName.toUnstyledString());
        if (!subserver.isReal) return FAILURE;

        MinigameMode mode;
        if (source.skipIfNext(" (")) {
            mode = MinigameMode.parse(source.readUntil(")"));
            if (!mode.isReal() || !source.skipIfNext("):")) return FAILURE;
        } else {
            mode = MinigameMode.UNKNOWN;
            if (!source.skipIfNext(":")) return FAILURE;
        }

        StyledString spaces = source.readWhile(" ");

        int playerCount;
        int playerLimit;
        if (source.skipIfNext(OFFLINE)) {
            playerCount = -1;
            playerLimit = -1;
        } else {
            playerCount = Util.parseIntOr(source.readUntil("/").toUnstyledString(), -1);
            if (playerCount < 0) return FAILURE;

            if (!source.skipIfNext("/")) return FAILURE;

            playerLimit = Util.parseIntOr(source.readAll().toUnstyledString(), -1);
            if (playerLimit < 0) return FAILURE;
        }

        return new GameTabSubserver(subserver, subserverName.isolate(), mode, spaces, playerCount, playerLimit, true);
    }
}
