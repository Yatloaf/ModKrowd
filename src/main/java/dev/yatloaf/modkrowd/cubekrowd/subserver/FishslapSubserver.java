package dev.yatloaf.modkrowd.cubekrowd.subserver;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeam;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.common.SelfPlayer;
import dev.yatloaf.modkrowd.util.text.StyledString;

public class FishslapSubserver extends MinigameSubserver {
    public FishslapSubserver(String id, String listName, String... tabNames) {
        super(id, listName, tabNames);
    }

    @Override
    public MinigameTeam teamFromColor(CKColor color) {
        return MinigameTeam.FS;
    }

    @Override
    public TextCache formatChat(String message) {
        StyledString prefix = SelfPlayer.rankNameSoft().appearance();
        if (StyledString.EMPTY.equals(prefix)) {
            return TextCache.EMPTY;
        } else {
            return TextCache.of(StyledString.concat(
                    StyledString.fromString("<"),
                    prefix,
                    StyledString.fromString("> " + message)
            ));
        }
    }
}
