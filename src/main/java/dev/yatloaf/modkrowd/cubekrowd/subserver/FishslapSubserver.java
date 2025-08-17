package dev.yatloaf.modkrowd.cubekrowd.subserver;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeam;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;

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
        return Subserver.formatChatMixed(message);
    }
}
