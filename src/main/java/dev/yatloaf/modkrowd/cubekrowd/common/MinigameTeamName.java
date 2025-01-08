package dev.yatloaf.modkrowd.cubekrowd.common;

import dev.yatloaf.modkrowd.cubekrowd.subserver.MinigameSubserver;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

import java.util.function.UnaryOperator;

public record MinigameTeamName(MinigameTeam team, StyledString name, boolean isReal) {
    public static final MinigameTeamName FAILURE = new MinigameTeamName(MinigameTeam.UNKNOWN, StyledString.EMPTY, false);

    public static MinigameTeamName readFast(StyledStringReader source, MinigameSubserver subserver) {
        StyledString name = source.readUntil(">").isolate();
        if (name.isEmpty()) return FAILURE;

        MinigameTeam team = subserver.teamFromColor(CKColor.fromStyle(name.firstStyle()));
        if (!team.isReal()) return FAILURE;

        return new MinigameTeamName(team, name, true);
    }

    public MinigameTeamName mapName(UnaryOperator<StyledString> mapper) {
        return new MinigameTeamName(this.team, mapper.apply(this.name), this.isReal);
    }

    public StyledString appearance() {
        return this.name;
    }
}
