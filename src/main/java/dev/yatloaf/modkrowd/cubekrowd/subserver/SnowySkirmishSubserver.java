package dev.yatloaf.modkrowd.cubekrowd.subserver;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeam;

public class SnowySkirmishSubserver extends MinigameSubserver {
    public SnowySkirmishSubserver(String id, String listName, String... tabNames) {
        super(id, listName, tabNames);
    }

    @Override
    public MinigameTeam teamFromColor(CKColor color) {
        return MinigameTeam.UNKNOWN;
    }
}
