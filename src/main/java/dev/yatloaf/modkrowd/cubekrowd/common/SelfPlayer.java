package dev.yatloaf.modkrowd.cubekrowd.common;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MinigameTabName;
import dev.yatloaf.modkrowd.util.text.StyledString;
import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public class SelfPlayer {
    public static RankName rankName = RankName.FAILURE;
    public static MinigameTeamName teamName = MinigameTeamName.FAILURE;

    public static RankName rankNameSoft() {
        if (ModKrowd.currentSubserver.isCubeKrowd) {
            RankName candidate = ModKrowd.TAB_DECO.tabHeaderFast().rankName();
            if (candidate.isReal()) {
                rankName = candidate;
            }
        }
        return rankName;
    }

    public static MinigameTeamName teamNameSoft() {
        if (ModKrowd.TAB_LIST.result().self() instanceof MinigameTabName minigameTabName) {
            teamName = minigameTabName.teamName();
        }
        return teamName;
    }

    public static String username() {
        return Minecraft.getInstance().getUser().getName();
    }

    public static StyledString tryFormat(String message) {
        return tryFormat(message, null);
    }

    public static StyledString tryFormat(String message, Style startStyle) {
        // TODO: Emoji

        FormattingPermission permission = SelfPlayer.rankNameSoft().rank().letters().formattingPermission();

        StyledString legacy = startStyle != null
                ? StyledString.fromFormattedString(message, '&', permission.legacy, startStyle)
                : StyledString.fromFormattedString(message, '&', permission.legacy);
        // Did that change anything?
        if (!legacy.equalsString(message)) return legacy;

        net.kyori.adventure.text.Component adventure = permission.miniMessage.deserialize(message);
        Component vanilla = MinecraftClientAudiences.of().asNative(adventure);
        return startStyle != null
                ? StyledString.fromText(vanilla, startStyle)
                : StyledString.fromText(vanilla);
    }
}
