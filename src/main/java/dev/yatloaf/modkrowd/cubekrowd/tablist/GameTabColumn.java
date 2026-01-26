package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.util.text.StyledString;

public record GameTabColumn(Subserver subserver, String subserverName, boolean isReal) implements TabEntry {
    public static final GameTabColumn FAILURE = new GameTabColumn(Subservers.UNKNOWN, "", false);

    public static final String YOUR_GAME = "Your game";

    public static GameTabColumn parseFast(StyledString source, Subserver yourGame) {
        String subserverName = source.toUnstyledString();
        if (YOUR_GAME.equals(subserverName)) {
            return new GameTabColumn(yourGame, subserverName, true);
        }

        Subserver subserver = Subservers.fromTabName(subserverName);
        if (!subserver.isReal) return FAILURE;

        return new GameTabColumn(subserver, subserverName, true);
    }

    public StyledString appearance() {
        return StyledString.fromString(this.subserverName, CKColor.GOLD.style.withBold(true));
    }
}
