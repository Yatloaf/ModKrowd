package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.util.Util;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public record MainTabPlayers(int playerCount, boolean isReal) implements TabEntry {
    public static final MainTabPlayers FAILURE = new MainTabPlayers(-1, false);

    public static final StyledString PLAYERS_ = StyledString.fromString("Players:", CKColor.YELLOW.style);

    public static MainTabPlayers readFast(StyledStringReader source) {
        source.skipUntilAfter(PLAYERS_);
        source.skipUntilAfter(" ");

        String playerCountString = source.readAll().toUnstyledString();
        if (playerCountString.isEmpty()) return FAILURE;

        int playerCount = Util.parseIntOr(playerCountString, -1);
        if (playerCount == -1) return FAILURE;

        return new MainTabPlayers(playerCount, true);
    }
}
