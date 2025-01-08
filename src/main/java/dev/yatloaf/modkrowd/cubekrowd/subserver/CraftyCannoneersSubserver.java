package dev.yatloaf.modkrowd.cubekrowd.subserver;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeam;

public class CraftyCannoneersSubserver extends MinigameSubserver {
    public CraftyCannoneersSubserver(String id, String listName, String... tabNames) {
        super(id, listName, tabNames);
    }

    @Override
    public MinigameTeam teamFromColor(CKColor color) {
        return switch (color) {
            case BLUE -> MinigameTeam.CC_LOBBY;
            case DARK_GRAY -> MinigameTeam.CC_SPECTATOR;
            case DARK_PURPLE -> MinigameTeam.CC_PURPLE;
            case GOLD -> MinigameTeam.CC_ORANGE;
            default -> MinigameTeam.UNKNOWN;
        };
    }
}
