package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.Afk;
import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeamName;
import dev.yatloaf.modkrowd.cubekrowd.subserver.MinigameSubserver;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

import java.util.function.UnaryOperator;

public record MinigameTabName(Afk afk, MinigameTeamName teamName, boolean isReal) implements TabEntry {
    public static final MinigameTabName FAILURE = new MinigameTabName(Afk.UNKNOWN, MinigameTeamName.FAILURE, false);

    public static MinigameTabName readFast(StyledStringReader source, MinigameSubserver subserver) {
        Afk afk = Afk.read(source);
        if (!afk.isReal()) return FAILURE;

        MinigameTeamName minigameTeamName = MinigameTeamName.readFast(source, subserver);
        if (!minigameTeamName.isReal()) return FAILURE;

        return new MinigameTabName(afk, minigameTeamName, true);
    }

    public StyledString appearance() {
        return StyledString.concat(
                this.afk.star,
                this.teamName.appearance()
        );
    }

    public MinigameTabName mapTeamName(UnaryOperator<MinigameTeamName> mapper) {
        return new MinigameTabName(this.afk, mapper.apply(this.teamName), this.isReal);
    }
}
