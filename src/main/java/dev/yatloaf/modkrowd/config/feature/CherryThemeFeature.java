package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.config.PredicateIndex;
import dev.yatloaf.modkrowd.config.queue.ActionQueue;
import dev.yatloaf.modkrowd.cubekrowd.common.Afk;
import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.CKStuff;
import dev.yatloaf.modkrowd.cubekrowd.common.LatencyLevel;
import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeam;
import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeamName;
import dev.yatloaf.modkrowd.cubekrowd.common.Rank;
import dev.yatloaf.modkrowd.cubekrowd.common.RankName;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.AfkMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.AlohaMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.DirectMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.MainChatMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.MinigameChatMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.MessageCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MainTabColumn;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MainTabName;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MainTabPing;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MainTabPlayers;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MinigameTabName;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabFooter;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabFooterSection;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabHeader;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabFooterCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabHeaderCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import dev.yatloaf.modkrowd.custom.Custom;
import dev.yatloaf.modkrowd.custom.SelfAlohaMessage;
import dev.yatloaf.modkrowd.util.Util;
import dev.yatloaf.modkrowd.util.text.StyledString;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

public class CherryThemeFeature extends ThemeFeature {
    public static final TextColor CHERRY1 = TextColor.fromRgb(0xe986bb);
    public static final TextColor CHERRY2 = TextColor.fromRgb(0xec95bf);
    public static final TextColor CHERRY3 = TextColor.fromRgb(0xefa7cd);
    public static final TextColor CHERRY4 = TextColor.fromRgb(0xf7b9dc);
    public static final TextColor CHERRY5 = TextColor.fromRgb(0xfccbe7);
    public static final TextColor CHERRY6 = TextColor.fromRgb(0xf5daef);

    public CherryThemeFeature(String id, PredicateIndex allowedPredicates) {
        super(id, allowedPredicates);
    }

    @Override
    public void onMessage(MessageCache message, boolean overlay, MinecraftClient client, ActionQueue queue) {
        switch (message.result()) {
            case AlohaMessage alohaMessage -> message.setThemed(this.alohaMessage(alohaMessage));
            case AfkMessage afkMessage -> message.setThemed(this.afkMessage(afkMessage));
            case DirectMessage directMessage -> message.setThemed(this.directMessage(directMessage));
            case MainChatMessage mainChatMessage -> message.setThemed(this.mainChatMessage(mainChatMessage));
            case MinigameChatMessage minigameChatMessage -> message.setThemed(this.minigameChatMessage(minigameChatMessage));
            default -> {}
        }
    }

    protected TextCache alohaMessage(AlohaMessage message) {
        return TextCache.of(message.appearance().fillColor(CHERRY5));
    }

    protected TextCache afkMessage(AfkMessage message) {
        return TextCache.of(message.appearance().fillColor(CHERRY2));
    }

    protected TextCache directMessage(DirectMessage message) {
        return TextCache.of(StyledString.concat(
                StyledString.fromString(message.sender() + " " + CKStuff.RIGHT_ARROW + " " + message.recipient() + " ").fillColor(CHERRY3),
                message.content().fillColor(CHERRY5)
        ));
    }

    protected TextCache mainChatMessage(MainChatMessage message) {
        return TextCache.of(StyledString.concat(
                this.rankName(message.sender()),
                StyledString.SPACE,
                message.content().mapStyle(this::whiteToCherry)
        ));
    }

    protected TextCache minigameChatMessage(MinigameChatMessage message) {
        return TextCache.of(StyledString.concat(
                StyledString.fromString("<"),
                this.minigameTeamName(message.teamName()),
                StyledString.fromString("> "),
                message.content().mapStyle(this::whiteToCherry)
        ));
    }

    @Override
    public void onTabList(TabListCache tabList, MinecraftClient client, ActionQueue queue) {
        for (TabEntryCache entry : tabList.result().entries()) {
            switch (entry.result()) {
                case MainTabName mainTabName -> entry.setThemed(this.mainTabName(mainTabName));
                case MainTabColumn mainTabColumn -> entry.setThemed(this.mainTabColumn(mainTabColumn));
                case MainTabPlayers mainTabPlayers -> entry.setThemed(this.mainTabPlayers(mainTabPlayers));
                case MainTabPing mainTabPing -> entry.setThemed(this.mainTabPing(mainTabPing));
                case MinigameTabName minigameTabName -> entry.setThemed(this.minigameTabName(minigameTabName));
                default -> {}
            }
            entry.setLatencyThemed(this.formatLatency(entry.latency));
        }
        TabHeaderCache tabHeader = tabList.tabHeaderCache();
        TabFooterCache tabFooter = tabList.tabFooterCache();
        tabHeader.setThemed(this.tabHeader(tabHeader.tabHeaderSoft()));
        tabFooter.setThemed(this.tabFooter(tabFooter.tabFooterSoft()));
    }

    protected TextCache mainTabName(MainTabName mainTabName) {
        return TextCache.of(StyledString.concat(
                this.afk(mainTabName.afk()),
                this.rankName(mainTabName.rankName())
        ));
    }

    protected TextCache mainTabColumn(MainTabColumn column) {
        return TextCache.of(column.appearance().fillColor(column.online() ? CHERRY4 : CHERRY1));
    }

    protected TextCache mainTabPlayers(MainTabPlayers players) {
        return TextCache.of(StyledString.concat(
                MainTabPlayers.PLAYERS_.fillColor(CHERRY5),
                StyledString.SPACE,
                StyledString.fromString(Integer.toString(players.playerCount()), Style.EMPTY.withColor(CHERRY6))
        ));
    }

    protected TextCache mainTabPing(MainTabPing ping) {
        return TextCache.of(StyledString.concat(
                MainTabPing.YOUR_PING_.fillColor(CHERRY5),
                StyledString.SPACE,
                StyledString.fromString(ping.latency() + "ms", Style.EMPTY.withColor(CHERRY6))
        ));
    }

    protected TextCache minigameTabName(MinigameTabName tabName) {
        return TextCache.of(StyledString.concat(
                this.afk(tabName.afk()),
                this.minigameTeamName(tabName.teamName())
        ));
    }

    protected TextCache tabHeader(TabHeader tabHeader) {
        return TextCache.of(StyledString.concat(
                TabHeader.PREFIX1.fillColor(CHERRY1),
                TabHeader.INFIX1.fillColor(CHERRY5),
                this.rankName(tabHeader.rankName()),
                TabHeader.SUFFIX1.fillColor(CHERRY1),
                StyledString.NEWLINE,
                TabHeader.PREFIX2.fillColor(CHERRY4),
                tabHeader.time().appearance().fillColor(CHERRY6)
        ));
    }

    protected TextCache tabFooter(TabFooter tabFooter) {
        TabFooterSection[] sections = tabFooter.sections();
        StyledString[] parts = new StyledString[sections.length * 2 - 1];
        StyledString seperator = StyledString.fromString(" | ", Style.EMPTY.withColor(CHERRY4));

        for (int i = 0; i < sections.length; i++) {
            if (i > 0) {
                parts[i * 2 - 1] = seperator;
            }
            parts[i * 2] = StyledString.concat(
                    sections[i].label(),
                    StyledString.SPACE,
                    sections[i].link().fillColor(CHERRY6)
            );
        }

        return TextCache.of(StyledString.concat(parts));
    }

    protected StyledString afk(Afk afk) {
        return afk.star.fillColor(CHERRY2);
    }

    protected StyledString rankName(RankName rankName) {
        Rank rank = rankName.rank();
        TextColor nameColor = this.colorNameFromRank(rank);
        TextColor bracketsColor = this.colorBrackets(rank);
        return StyledString.concat(
                StyledString.concat(
                        rank.pipes().pipe.fillColor(CHERRY4),
                        rank.brackets().leftBracket.fillColor(bracketsColor),
                        rank.letters().letter.fillColor(CHERRY6),
                        rank.brackets().rightBracket.fillColor(bracketsColor),
                        rank.pipes().pipe.fillColor(CHERRY4)
                ),
                StyledString.SPACE,
                rankName.name().fillColor(nameColor)
        );
    }

    protected StyledString minigameTeamName(MinigameTeamName teamName) {
        TextColor teamColor = this.colorNameFromTeam(teamName.team());
        return teamName.name().fillColor(teamColor);
    }

    protected Style whiteToCherry(Style style) {
        return CKColor.WHITE.textColor.equals(style.getColor()) ? style.withColor(CHERRY6) : style;
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
