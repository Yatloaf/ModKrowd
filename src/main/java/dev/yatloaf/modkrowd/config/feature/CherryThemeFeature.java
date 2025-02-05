package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.config.PredicateIndex;
import dev.yatloaf.modkrowd.config.queue.ActionQueue;
import dev.yatloaf.modkrowd.cubekrowd.common.Afk;
import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.LatencyLevel;
import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeam;
import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeamName;
import dev.yatloaf.modkrowd.cubekrowd.common.Rank;
import dev.yatloaf.modkrowd.cubekrowd.common.RankName;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MainTabColumn;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MainTabName;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MainTabPing;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MinigameTabName;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
import dev.yatloaf.modkrowd.custom.Custom;
import dev.yatloaf.modkrowd.custom.SelfAlohaMessage;
import dev.yatloaf.modkrowd.util.Util;
import dev.yatloaf.modkrowd.util.text.StyledString;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

public class CherryThemeFeature extends CherryLiteThemeFeature {
    public CherryThemeFeature(String id, PredicateIndex allowedPredicates) {
        super(id, allowedPredicates);
    }

    @Override
    protected void onTabEntry(TabEntryCache entry) {
        super.onTabEntry(entry);
        switch (entry.result()) {
            case MainTabName mainTabName -> entry.setThemed(this.mainTabName(mainTabName));
            case MinigameTabName minigameTabName -> entry.setThemed(this.minigameTabName(minigameTabName));
            default -> {}
        }
        entry.setLatencyThemed(this.formatLatency(entry.latency));
    }

    @Override
    protected TextCache mainTabColumn(MainTabColumn column) {
        return TextCache.of(column.appearance().fillColor(column.online() ? CHERRY4 : CHERRY1));
    }

    @Override
    protected TextCache mainTabPing(MainTabPing ping) {
        return TextCache.of(StyledString.concat(
                MainTabPing.YOUR_PING_.fillColor(CHERRY5),
                StyledString.fromString(" " + ping.latency() + "ms", Style.EMPTY.withColor(CHERRY6))
        ));
    }

    protected TextCache mainTabName(MainTabName mainTabName) {
        return TextCache.of(StyledString.concat(
                this.afk(mainTabName.afk()),
                this.rankName(mainTabName.rankName())
        ));
    }

    protected TextCache minigameTabName(MinigameTabName tabName) {
        return TextCache.of(StyledString.concat(
                this.afk(tabName.afk()),
                this.minigameTeamName(tabName.teamName())
        ));
    }

    protected StyledString afk(Afk afk) {
        return afk.star.fillColor(CHERRY2);
    }

    @Override
    protected StyledString rankName(RankName rankName) {
        Rank rank = rankName.rank();
        TextColor nameColor = this.colorNameFromRank(rank);
        TextColor bracketsColor = this.colorBrackets(rank);
        return StyledString.concat(
                rank.pipes().pipe.fillColor(CHERRY4),
                rank.brackets().leftBracket.fillColor(bracketsColor),
                rank.letters().letter.fillColor(CHERRY6),
                rank.brackets().rightBracket.fillColor(bracketsColor),
                rank.pipes().pipe.fillColor(CHERRY4),
                StyledString.SPACE,
                rankName.name().fillColor(nameColor)
        );
    }

    @Override
    protected StyledString minigameTeamName(MinigameTeamName teamName) {
        TextColor teamColor = this.colorNameFromTeam(teamName.team());
        return teamName.name().fillColor(teamColor);
    }

    protected TextColor colorBrackets(Rank rank) {
        return switch (rank.brackets()) {
            case NONE -> CHERRY1;
            case YELLOW -> CHERRY5;
            case GOLD -> CHERRY3;
            default -> CKColor.WHITE.textColor;
        };
    }

    @SuppressWarnings("DuplicateBranchesInSwitch")
    protected TextColor colorNameFromRank(Rank rank) {
        return switch (rank.letters()) {
            case GUEST -> CHERRY5;
            case MEMBER, BUILDER -> CHERRY6;
            case TRUSTED -> CHERRY3;
            case RESPECTED -> CHERRY2;
            case VETERAN, DEVELOPER -> CHERRY1;
            case SPECIAL_GUEST, YOUTUBE, ZIPKROWD -> CHERRY4;
            case HELPER -> CHERRY3;
            case MODERATOR -> CHERRY3;
            case ADMIN -> CHERRY5;
            default -> CKColor.WHITE.textColor;
        };
    }

    protected TextColor colorNameFromTeam(MinigameTeam team) {
        return switch (team) {
            case MW_LOBBY, RR_LOBBY, CC_LOBBY -> CHERRY1;
            case MW_SPECTATOR, RR_SPECTATOR, IR_SPECTATOR, FS, CC_SPECTATOR -> CHERRY4;
            case MW_RED, RR_BLUE, IR_RED, CC_PURPLE -> CHERRY3;
            case MW_GREEN, RR_YELLOW, IR_GREEN, CC_ORANGE -> CHERRY6;
            default -> CKColor.WHITE.textColor;
        };
    }

    protected TextCache formatLatency(int latency) {
        TextColor color = this.colorLatencyLevel(LatencyLevel.fromLatency(latency));
        return TextCache.of(Text.literal(Util.superscript(latency)).setStyle(Style.EMPTY.withColor(color)));
    }

    protected TextColor colorLatencyLevel(LatencyLevel level) {
        return switch (level) {
            case UNKNOWN -> CHERRY6;
            case L1 -> CHERRY5;
            case L2 -> CHERRY4;
            case L3 -> CHERRY3;
            case L4 -> CHERRY2;
            case L5 -> CHERRY1;
        };
    }

    @Override
    public TextCache themeCustom(Custom custom, MinecraftClient client, ActionQueue queue) {
        return switch (custom) {
            case SelfAlohaMessage selfAlohaMessage -> this.selfAlohaMessage(selfAlohaMessage);
            default -> TextCache.EMPTY;
        };
    }

    public TextCache selfAlohaMessage(SelfAlohaMessage selfAlohaMessage) {
        return TextCache.of(selfAlohaMessage.appearance().text().setStyle(Style.EMPTY.withColor(CHERRY5)));
    }
}
