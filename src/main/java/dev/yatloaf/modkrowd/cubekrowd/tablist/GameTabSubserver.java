package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.util.text.StyledString;

public record GameTabSubserver(Subserver subserver, StyledString subserverName, boolean isReal) implements TabEntry {
    public static final GameTabSubserver FAILURE = new GameTabSubserver(Subservers.UNKNOWN, StyledString.EMPTY, false);

    public static GameTabSubserver parse(StyledString source) {
        Subserver subserver = Subservers.fromTabName(source.toUnstyledString());
        if (!subserver.isReal) return FAILURE;

        return new GameTabSubserver(subserver, source.isolate(), true);
    }
}
