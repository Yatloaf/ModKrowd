package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.config.Restriction;
import dev.yatloaf.modkrowd.cubekrowd.common.Afk;
import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.LatencyLevel;
import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeam;
import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeamName;
import dev.yatloaf.modkrowd.cubekrowd.common.Rank;
import dev.yatloaf.modkrowd.cubekrowd.common.RankName;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.DeathMessage;
import dev.yatloaf.modkrowd.cubekrowd.tablist.GameTabMinigame;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MainTabColumn;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MainTabName;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MinigameTabName;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabPing;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import dev.yatloaf.modkrowd.custom.Custom;
import dev.yatloaf.modkrowd.custom.MissileWarsTieMessage;
import dev.yatloaf.modkrowd.custom.SelfAlohaMessage;
import dev.yatloaf.modkrowd.util.Util;
import dev.yatloaf.modkrowd.util.text.StyledString;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

public class CherryThemeFeature extends CherryLiteThemeFeature {
    public CherryThemeFeature(String id, Restriction restriction) {
        super(id, restriction);
    }

    @Override
    protected TextCache deathMessage(DeathMessage message) {
        DeathMessage modifiedMessage = message
                .mapVictim(this::modifyMissileWarsTeamName)
                .mapKillerIfPresent(this::modifyMissileWarsTeamName);
        return super.deathMessage(modifiedMessage);
    }

    // Someone clean up this spaghetti
    protected Component modifyMissileWarsTeamName(Component original) {
        return switch (CKColor.fromStyle(original.getStyle())) {
            case RED -> original.copy().withStyle(Style.EMPTY.withColor(CHERRY3));
            case GREEN -> original.copy().withStyle(Style.EMPTY.withColor(CHERRY6));
            case null, default -> original;
        };
    }

    @Override
    protected void onTabEntry(TabListCache tabList, TabEntryCache entry) {
        super.onTabEntry(tabList, entry);
        switch (entry.result()) {
            case MainTabName mainTabName -> entry.setNameThemed(this.mainTabName(mainTabName));
            case MinigameTabName minigameTabName -> entry.setNameThemed(this.minigameTabName(minigameTabName));
            case GameTabMinigame gameTabMinigame -> entry.setNameThemed(this.gameTabMinigame(gameTabMinigame));
            default -> {}
        }
        entry.setLatencyThemed(this.formatLatency(entry.latency));
    }

    @Override
    protected TextCache tabPing(TabPing ping) {
        return TextCache.of(StyledString.concat(
                TabPing.YOUR_PING_.fillColor(CHERRY5),
                StyledString.fromString(" " + ping.latency() + "ms", Style.EMPTY.withColor(CHERRY6))
        ));
    }

    @Override
    protected TextCache mainTabColumn(MainTabColumn column) {
        return TextCache.of(column.appearance().fillColor(column.online() ? CHERRY4 : CHERRY1));
    }

    @Override
    protected StyledString gameTabSubserverName(StyledString name) {
        return name.mapStyle(this::styleMinigame);
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

    protected TextCache gameTabMinigame(GameTabMinigame gameTabMinigame) {
        return TextCache.of(gameTabMinigame.minigameName().mapStyle(this::styleMinigame));
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

    @Override
    protected StyledString cubeKrowd(StyledString cubeKrowd) {
        return cubeKrowd.mapStyle(style -> switch (CKColor.fromStyle(style)) {
            case DEPRESSED_DARK_AQUA -> style.withColor(CHERRY1);
            case DEPRESSED_LAVENDER -> style.withColor(CHERRY2);
            case DEPRESSED_GOLD -> style.withColor(CHERRY3);
            case DARK_AQUA -> style.withColor(CHERRY4);
            case LAVENDER -> style.withColor(CHERRY5);
            case GOLD -> style.withColor(CHERRY6);
            case null, default -> style;
        });
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

    protected Style styleMinigame(Style style) {
        return switch (CKColor.fromStyle(style)) {
            case DARK_PURPLE -> style.withColor(CHERRY1);
            case BLUE, RED -> style.withColor(CHERRY2);
            case DARK_AQUA, CRIMSON -> style.withColor(CHERRY3);
            case GOLD -> style.withColor(CHERRY4);
            case GREEN, SKY -> style.withColor(CHERRY5);
            case AQUA, SILVER -> style.withColor(CHERRY6);
            case null, default -> style;
        };
    }

    protected TextCache formatLatency(int latency) {
        TextColor color = this.colorLatencyLevel(LatencyLevel.fromLatency(latency));
        return TextCache.of(Component.literal(Util.superscript(latency)).setStyle(Style.EMPTY.withColor(color)));
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
    public TextCache themeCustom(Custom custom, Minecraft minecraft, ActionQueue queue) {
        return switch (custom) {
            case SelfAlohaMessage selfAlohaMessage -> this.selfAlohaMessage(selfAlohaMessage);
            case MissileWarsTieMessage missileWarsTieMessage -> this.missileWarsTieMessage(missileWarsTieMessage);
            default -> TextCache.EMPTY;
        };
    }

    public TextCache selfAlohaMessage(SelfAlohaMessage selfAlohaMessage) {
        return TextCache.of(selfAlohaMessage.appearance().text().setStyle(Style.EMPTY.withColor(CHERRY5)));
    }

    // DRY!!

    private static final MutableComponent MW_TIE_RED =
            MissileWarsTieMessage.RED.copy().setStyle(Style.EMPTY.withColor(CHERRY3));
    private static final MutableComponent MW_TIE_GREEN =
            MissileWarsTieMessage.GREEN.copy().setStyle(Style.EMPTY.withColor(CHERRY6));
    private static final TextCache MW_TIE_SIMULTANEOUS = TextCache.of(
            MissileWarsTieMessage.SIMULTANEOUS.text().copy().setStyle(Style.EMPTY.withColor(CHERRY4).withItalic(true))
    );

    public TextCache missileWarsTieMessage(MissileWarsTieMessage missileWarsTieMessage) {
        long redWinTick = missileWarsTieMessage.redWinTick();
        long greenWinTick = missileWarsTieMessage.greenWinTick();

        if (redWinTick == greenWinTick) {
            return MW_TIE_SIMULTANEOUS;
        } else {
            long deltaTicks;
            MutableComponent first;
            MutableComponent last;

            if (redWinTick < greenWinTick) {
                deltaTicks = greenWinTick - redWinTick;
                first = MW_TIE_RED;
                last = MW_TIE_GREEN;
            } else {
                deltaTicks = redWinTick - greenWinTick;
                first = MW_TIE_GREEN;
                last = MW_TIE_RED;
            }

            return TextCache.of(MissileWarsTieMessage.sequential(first, last, deltaTicks)
                    .setStyle(Style.EMPTY.withColor(CHERRY2).withItalic(true)));
        }
    }
}
