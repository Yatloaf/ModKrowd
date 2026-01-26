package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.util.text.StyledString;

public interface TabEntry {
    TabEntry FAILURE = new TabEntry() {};
    TabEntry[] EMPTY = {};

    default boolean isPlayer() {
        return !this.playerName().isEmpty();
    }

    default StyledString playerName() {
        return StyledString.EMPTY;
    }

    default Subserver playerSubserver() {
        return Subservers.UNKNOWN;
    }
}
