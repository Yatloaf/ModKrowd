package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.util.Util;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public record GameTabPlayers(int playerCount, int playerLimit, boolean isReal) implements TabEntry {
    public static final GameTabPlayers FAILURE = new GameTabPlayers(-1, -1, false);

    public static GameTabPlayers readSoft(StyledStringReader source) {
        int playerCount = Util.parseIntOr(source.readUntil("/").toUnstyledString(), -1);
        source.skipUntilAfter("/");
        int playerLimit = Util.parseIntOr(source.readAll().toUnstyledString(), -1);

        return new GameTabPlayers(playerCount, playerLimit, playerCount != -1 && playerLimit != -1);
    }
}
