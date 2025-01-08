package dev.yatloaf.modkrowd.cubekrowd.subserver;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeam;

public class RocketRidersSubserver extends MinigameSubserver {
    public RocketRidersSubserver(String id, String listName, String... tabNames) {
        super(id, listName, tabNames);
    }

    @Override
    public MinigameTeam teamFromColor(CKColor color) {
        return switch (color) {
            case GREEN -> MinigameTeam.RR_LOBBY;
            case DARK_GRAY -> MinigameTeam.RR_SPECTATOR;
            case BLUE -> MinigameTeam.RR_BLUE;
            case GOLD -> MinigameTeam.RR_YELLOW;
            default -> MinigameTeam.UNKNOWN;
        };
    }
}
