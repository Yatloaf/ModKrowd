package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.Afk;
import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeamName;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

import java.util.function.UnaryOperator;

public record MinigameTabName(Afk afk, StyledString prefix, MinigameTeamName teamName, Subserver subserver, boolean isReal) implements TabEntry {
    public static final MinigameTabName FAILURE = new MinigameTabName(Afk.UNKNOWN, StyledString.EMPTY, MinigameTeamName.FAILURE, Subservers.UNKNOWN, false);

    public static MinigameTabName readFast(StyledStringReader source, Subserver subserver) {
        Afk afk = Afk.read(source);
        if (!afk.isReal()) return FAILURE;

        StyledString prefix;
        if (subserver.tabPrefixes && source.peekAll().contains(" ")) {
            prefix = source.readUntilAfter(" ");
        } else {
            prefix = StyledString.EMPTY;
        }

        // *Sometimes*, this uses legacy formatting codes
        // This is now handled by `StyledString` upfront
        MinigameTeamName minigameTeamName = MinigameTeamName.readFast(source, subserver);
        if (!minigameTeamName.isReal() || !source.isAtEnd()) return FAILURE;

        return new MinigameTabName(afk, prefix, minigameTeamName, subserver, true);
    }

    public StyledString appearance() {
        return StyledString.concat(
                this.afk.star,
                this.prefix,
                this.teamName.appearance()
        );
    }

    public MinigameTabName mapTeamName(UnaryOperator<MinigameTeamName> mapper) {
        return new MinigameTabName(this.afk, this.prefix, mapper.apply(this.teamName), this.subserver, this.isReal);
    }

    @Override
    public StyledString playerName() {
        return this.teamName.name();
    }

    @Override
    public Subserver playerSubserver() {
        return this.subserver;
    }
}
