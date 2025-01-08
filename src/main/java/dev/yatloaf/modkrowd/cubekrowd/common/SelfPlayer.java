package dev.yatloaf.modkrowd.cubekrowd.common;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.cubekrowd.subserver.CubeKrowdSubserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.MinigameSubserver;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;
import net.minecraft.client.MinecraftClient;

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
        return MinecraftClient.getInstance().getSession().getUsername();
    }
}
