package dev.yatloaf.modkrowd.cubekrowd.subserver;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeam;
import net.minecraft.client.network.ClientPlayNetworkHandler;

public class MissileWarsSubserver extends MinigameSubserver {
    private static final Subserver[] EMPTY = new Subserver[0];

    public Subserver[] nextSubservers = EMPTY; // Has to be initialized late to establish cycles

    public MissileWarsSubserver(String id, String listName, String... tabNames) {
        super(id, listName, tabNames);
    }

    @Override
    public MinigameTeam teamFromColor(CKColor color) {
        return switch (color) {
            case GRAY -> MinigameTeam.MW_LOBBY;
            case BLUE -> MinigameTeam.MW_SPECTATOR;
            case RED -> MinigameTeam.MW_RED;
            case GREEN -> MinigameTeam.MW_GREEN;
            default -> MinigameTeam.UNKNOWN;
        };
    }

    public boolean tryConnectNext(ClientPlayNetworkHandler handler, int index) {
        if (this.nextSubservers.length <= index) {
            return false;
        } else {
            this.nextSubservers[index].connect(handler);
            return true;
        }
    }
}
