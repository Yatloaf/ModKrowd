package dev.yatloaf.modkrowd.cubekrowd.common;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.cubekrowd.subserver.CubeKrowdSubserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.MinigameSubserver;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Style;

import java.util.EnumSet;

public class SelfPlayer {
    public static RankName rankName = RankName.FAILURE;
    public static MinigameTeamName teamName = MinigameTeamName.FAILURE;

    public static RankName rankNameSoft() {
        if (ModKrowd.currentSubserver instanceof CubeKrowdSubserver) {
            RankName candidate = ModKrowd.currentTabListCache.tabHeaderCache().tabHeaderSoft().rankName();
            if (candidate.isReal()) {
                rankName = candidate;
            }
        }
        return rankName;
    }

    public static MinigameTeamName teamNameSoft() {
        if (ModKrowd.currentSubserver instanceof MinigameSubserver minigameSubserver) {
            TabEntryCache self = ModKrowd.currentTabListCache.result().self();
            if (self != null) {
                MinigameTeamName candidate = MinigameTeamName.readFast(StyledStringReader.of(self.original.styledString()), minigameSubserver);
                if (candidate.isReal()) {
                    teamName = candidate;
                }
            }
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
