package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.subserver.Minigame;
import dev.yatloaf.modkrowd.util.text.StyledString;

public record GameTabMinigame(Minigame minigame, StyledString minigameName, boolean isReal) implements TabEntry {
    public static final GameTabMinigame FAILURE = new GameTabMinigame(Minigame.UNKNOWN, StyledString.EMPTY, false);

    public static GameTabMinigame parseFast(StyledString source) {
        Minigame minigame = Minigame.fromTabName(source.toUnstyledString());
        if (!minigame.isReal()) return FAILURE;

        return new GameTabMinigame(minigame, source.isolate(), true);
    }
}
