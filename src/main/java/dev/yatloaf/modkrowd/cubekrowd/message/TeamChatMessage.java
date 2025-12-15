package dev.yatloaf.modkrowd.cubekrowd.message;

import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeamName;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public record TeamChatMessage(MinigameTeamName teamName, StyledString content, boolean isReal) implements Message {
    public static final TeamChatMessage FAILURE = new TeamChatMessage(MinigameTeamName.FAILURE, StyledString.EMPTY, false);

    public static TeamChatMessage readFast(StyledStringReader source, Subserver subserver) {
        if (!source.skipIfNext("<")) return FAILURE;

        MinigameTeamName teamName = MinigameTeamName.readFast(source, subserver);
        if (!teamName.isReal()) return FAILURE;

        source.skipUntilAfter("> ");

        StyledString content = source.readAll().isolate();
        if (content.isEmpty()) return FAILURE;

        return new TeamChatMessage(teamName, content, true);
    }
}
