package dev.yatloaf.modkrowd.cubekrowd.subserver;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeam;

public class IceRunnerSubserver extends MinigameSubserver {
    public IceRunnerSubserver(String id, String listName, String... tabNames) {
        super(id, listName, tabNames);
    }

    @Override
    public MinigameTeam teamFromColor(CKColor color) {
        return switch (color) {
            case WHITE -> MinigameTeam.IR_SPECTATOR;
            case RED -> MinigameTeam.IR_RED;
            case GREEN -> MinigameTeam.IR_GREEN;
            default -> MinigameTeam.UNKNOWN;
        };
    }
}
