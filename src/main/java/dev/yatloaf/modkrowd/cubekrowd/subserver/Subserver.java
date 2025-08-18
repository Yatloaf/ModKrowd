package dev.yatloaf.modkrowd.cubekrowd.subserver;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeamName;
import dev.yatloaf.modkrowd.cubekrowd.common.SelfPlayer;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.util.Util;
import dev.yatloaf.modkrowd.util.text.StyledString;
import net.minecraft.client.network.ClientPlayNetworkHandler;

public abstract class Subserver {
    // Shoutout to the CubeKrowd developers for giving each server at least 6 names:
    // id           [/server]
    // listName     [/glist]
    // tabNames     Tab List, BuildComp Tab List
    // ---          [/cklist], Connecting to...

    private static final String CONNECT_COMMAND = "connect ";
    public final String id;
    public final String listName;
    public final String[] tabNames;

    public Subserver(String id, String listName, String... tabNames) {
        this.id = id;
        this.tabNames = tabNames;
        this.listName = listName;
    }

    public boolean isReal() {
        return false;
    }

    public void connect(ClientPlayNetworkHandler handler) {
        if (this.id != null) {
            Util.sendCommandPacket(handler, CONNECT_COMMAND + this.id);
        } else {
            ModKrowd.LOGGER.error("[Subserver] Called .connect() on {}", this);
        }
    }

    public TextCache formatChat(String message) {
        return TextCache.EMPTY;
    }

    public static TextCache formatChatMain(String message) {
        StyledString prefix = SelfPlayer.rankNameSoft().appearance();

        if (StyledString.EMPTY.equals(prefix)) {
            return TextCache.EMPTY;
        } else {
            StyledString formatted = SelfPlayer.tryFormat(message);
            return TextCache.of(StyledString.concat(prefix, StyledString.SPACE, formatted));
        }
    }

    public static TextCache formatChatMinigame(String message) {
        MinigameTeamName teamName = SelfPlayer.teamNameSoft();

        if (teamName != MinigameTeamName.FAILURE) {
            StyledString formatted = SelfPlayer.tryFormat(message);
            return TextCache.of(StyledString.concat(
                    StyledString.fromString("<"),
                    teamName.appearance(),
                    StyledString.fromString("> "),
                    formatted
            ));
        } else {
            return TextCache.EMPTY;
        }
    }

    public static TextCache formatChatMixed(String message) {
        StyledString prefix = SelfPlayer.rankNameSoft().appearance();

        if (StyledString.EMPTY.equals(prefix)) {
            return TextCache.EMPTY;
        } else {
            StyledString formatted = SelfPlayer.tryFormat(message);
            return TextCache.of(StyledString.concat(
                    StyledString.fromString("<"),
                    prefix,
                    StyledString.fromString("> "),
                    formatted
            ));
        }
    }
}
