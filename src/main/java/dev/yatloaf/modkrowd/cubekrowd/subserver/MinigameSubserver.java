package dev.yatloaf.modkrowd.cubekrowd.subserver;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeam;
import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeamName;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.common.SelfPlayer;
import dev.yatloaf.modkrowd.util.text.StyledString;

public abstract class MinigameSubserver extends RealSubserver {
    public MinigameSubserver(String id, String listName, String... tabNames) {
        super(id, listName, tabNames);
    }

    abstract public MinigameTeam teamFromColor(CKColor color);

    @Override
    public TextCache formatChat(String message) {
        MinigameTeamName teamName = SelfPlayer.teamNameSoft();
        if (teamName != MinigameTeamName.FAILURE) {
            return TextCache.of(StyledString.concat(
                    StyledString.fromString("<"),
                    teamName.appearance(),
                    StyledString.fromString("> " + message)
            ));
        } else {
            return TextCache.EMPTY;
        }
    }
}
