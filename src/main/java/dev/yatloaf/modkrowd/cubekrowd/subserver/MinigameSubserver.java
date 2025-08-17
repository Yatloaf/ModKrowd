package dev.yatloaf.modkrowd.cubekrowd.subserver;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeam;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;

public abstract class MinigameSubserver extends RealSubserver {
    public MinigameSubserver(String id, String listName, String... tabNames) {
        super(id, listName, tabNames);
    }

    abstract public MinigameTeam teamFromColor(CKColor color);

    @Override
    public TextCache formatChat(String message) {
        return Subserver.formatChatMinigame(message);
    }
}
