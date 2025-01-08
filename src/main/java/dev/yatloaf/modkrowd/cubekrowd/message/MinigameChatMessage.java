package dev.yatloaf.modkrowd.cubekrowd.message;

import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeamName;
import dev.yatloaf.modkrowd.cubekrowd.subserver.MinigameSubserver;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public record MinigameChatMessage(MinigameTeamName teamName, StyledString content, boolean isReal) implements Message {
    public static final MinigameChatMessage FAILURE = new MinigameChatMessage(MinigameTeamName.FAILURE, StyledString.EMPTY, false);

    public static MinigameChatMessage readFast(StyledStringReader source, MinigameSubserver subserver) {
        if (!source.skipIfNext("<")) return FAILURE;

        MinigameTeamName teamName = MinigameTeamName.readFast(source, subserver);
        if (!teamName.isReal()) return FAILURE;

        source.skipUntilAfter("> ");

        StyledString content = source.readAll().isolate();
        if (content.isEmpty()) return FAILURE;

        return new MinigameChatMessage(teamName, content, true);
    }
}
