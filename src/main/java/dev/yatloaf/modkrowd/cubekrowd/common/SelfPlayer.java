package dev.yatloaf.modkrowd.cubekrowd.common;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MinigameTabName;
import dev.yatloaf.modkrowd.util.text.StyledString;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Style;

import java.util.EnumSet;

public class SelfPlayer {
    public static RankName rankName = RankName.FAILURE;
    public static MinigameTeamName teamName = MinigameTeamName.FAILURE;

    public static RankName rankNameSoft() {
        if (ModKrowd.currentSubserver.isCubeKrowd) {
            RankName candidate = ModKrowd.TAB_DECO.tabHeaderSoft().rankName();
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
        EnumSet<ChatFormatting> permittedFormattings = SelfPlayer.rankNameSoft().rank().letters().permittedFormattings();
        return StyledString.fromFormattedString(message, '&', permittedFormattings);
    }

    public static StyledString tryFormat(String message, Style startStyle) {
        EnumSet<ChatFormatting> permittedFormattings = SelfPlayer.rankNameSoft().rank().letters().permittedFormattings();
        return StyledString.fromFormattedString(message, '&', permittedFormattings, startStyle);
    }
}
