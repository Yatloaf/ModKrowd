package dev.yatloaf.modkrowd.cubekrowd.subserver;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeam;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.util.Util;
import net.minecraft.client.multiplayer.ClientPacketListener;

public class Subserver {
    // Shoutout to the CubeKrowd developers for giving each server at least 6 names:
    // id           [/server]
    // listName     [/glist]
    // tabNames     Tab List, BuildComp Tab List
    // ---          [/cklist], Connecting to...

    private static final String CONNECT_COMMAND = "connect %s";

    public final String id;
    public final String listName;
    public final String[] tabNames;

    public final Minigame minigame;
    private final FormatChat formatChat;

    public final boolean allowCheats;
    public final boolean hasChattymotes;
    public final boolean isMinigame;
    public final boolean isCubeKrowd;
    public final boolean isReal;

    public Subserver[] nextSubservers = null;

    public Subserver(
            String id,
            String listName,
            String[] tabNames,
            Minigame minigame,
            FormatChat formatChat,
            boolean allowCheats,
            boolean hasChattymotes,
            boolean isMinigame,
            boolean isCubeKrowd,
            boolean isReal
    ) {
        this.id = id;
        this.listName = listName;
        this.tabNames = tabNames;
        this.minigame = minigame;
        this.formatChat = formatChat;
        this.allowCheats = allowCheats;
        this.hasChattymotes = hasChattymotes;
        this.isMinigame = isMinigame;
        this.isCubeKrowd = isCubeKrowd;
        this.isReal = isReal;
    }

    public static SubserverBuilder builder() {
        return new SubserverBuilder();
    }

    public TextCache formatChat(String message) {
        return this.formatChat.formatChat(message);
    }

    public MinigameTeam teamFromColor(CKColor color) {
        return this.minigame.teamFromColor(color);
    }

    public void connect(ClientPacketListener listener) {
        if (this.id != null) {
            Util.sendCommandPacket(listener, CONNECT_COMMAND.formatted(this.id));
        }
    }

    /**
     * Try to connect to the next subserver.
     * @param listener The {@link ClientPacketListener} for sending the command packet.
     * @param index The number of times this has been unsuccessfully tried so far.
     * @return The subserver that is now being connected to, or {@code null} if there is no subserver left.
     */
    public Subserver tryConnectNext(ClientPacketListener listener, int index) {
        if (this.nextSubservers == null || this.nextSubservers.length <= index) {
            return null;
        } else {
            Subserver destination = this.nextSubservers[index];
            destination.connect(listener);
            return destination;
        }
    }
}
