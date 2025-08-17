package dev.yatloaf.modkrowd.cubekrowd.subserver;

import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;

public class BuildTeamSubserver extends CreativeSubserver {
    public BuildTeamSubserver(String id, String listName, String... tabNames) {
        super(id, listName, tabNames);
    }

    @Override
    public TextCache formatChat(String message) {
        return Subserver.formatChatMixed(message);
    }
}
