package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.config.DefaultTheme;
import dev.yatloaf.modkrowd.config.PredicateIndex;
import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.CubeKrowd;
import dev.yatloaf.modkrowd.cubekrowd.common.LatencyLevel;
import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeamName;
import dev.yatloaf.modkrowd.cubekrowd.common.RankName;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.AfkMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.AlohaMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.ConnectingMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.DirectMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.MixedChatMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.RankChatMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.TeamChatMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.DeathMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.MessageCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.GameTabColumn;
import dev.yatloaf.modkrowd.cubekrowd.tablist.GameTabSubserver;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MinigameMode;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabDecoCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabLiteral;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MainTabColumn;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabPlayers;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabFooter;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabFooterSection;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabHeader;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabPing;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import dev.yatloaf.modkrowd.util.text.StyledString;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

public class CherryLiteThemeFeature extends ThemeFeature {
    public static final TextColor CHERRY1 = TextColor.fromRgb(0xe986bb);
    public static final TextColor CHERRY2 = TextColor.fromRgb(0xec95bf);
    public static final TextColor CHERRY3 = TextColor.fromRgb(0xefa7cd);
    public static final TextColor CHERRY4 = TextColor.fromRgb(0xf7b9dc);
    public static final TextColor CHERRY5 = TextColor.fromRgb(0xfccbe7);
    public static final TextColor CHERRY6 = TextColor.fromRgb(0xf5daef);

    public static final int STEM_T = 0x80_27_16_20;
    public static final int CHERRY6_T = 0x20_00_00_00 | CHERRY6.getValue();

    public static final int STEM_SHADOW = 0xFF_00_00_00 | STEM_T;

    public static final TextCache LITERAL_SERVERS = TextCache.of(TabLiteral.SERVERS.text.fillColor(CHERRY4));

    public CherryLiteThemeFeature(String id, PredicateIndex allowedPredicates) {
        super(id, allowedPredicates);
    }

    @Override
    public void onMessage(MessageCache message, Minecraft minecraft, ActionQueue queue) {
        message.setBackgroundTint(STEM_T);
        switch (message.result()) {
            case AlohaMessage alohaMessage -> message.setThemed(this.alohaMessage(alohaMessage));
            case AfkMessage afkMessage -> message.setThemed(this.afkMessage(afkMessage));
            case DirectMessage directMessage -> message.setThemed(this.directMessage(directMessage));
            case ConnectingMessage connectingMessage -> message.setThemed(this.connectingMessage(connectingMessage));
            case RankChatMessage rankChatMessage -> message.setThemed(this.rankChatMessage(rankChatMessage));
            case TeamChatMessage teamChatMessage -> message.setThemed(this.teamChatMessage(teamChatMessage));
            case DeathMessage deathMessage -> message.setThemed(this.deathMessage(deathMessage));
            case MixedChatMessage mixedChatMessage -> message.setThemed(this.mixedChatMessage(mixedChatMessage));
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
                StyledString.fromString(message.sender() + " " + CubeKrowd.RIGHT_ARROW + " " + message.recipient() + " ").fillColor(CHERRY3),
                message.content().fillColor(CHERRY5)
        ));
    }

    protected TextCache connectingMessage(ConnectingMessage message) {
        return TextCache.of(message.appearance().fillColor(CHERRY4));
    }

    protected TextCache rankChatMessage(RankChatMessage message) {
        return TextCache.of(StyledString.concat(
                this.rankName(message.sender()),
                StyledString.SPACE,
                message.content().mapStyle(this::whiteToCherry)
        ));
    }

    protected TextCache teamChatMessage(TeamChatMessage message) {
        return TextCache.of(StyledString.concat(
                StyledString.fromString("<").fillColor(CHERRY6),
                this.minigameTeamName(message.teamName()),
                StyledString.fromString("> ").fillColor(CHERRY6),
                message.content().mapStyle(this::whiteToCherry)
        ));
    }

    protected TextCache deathMessage(DeathMessage message) {
        return TextCache.of(message.appearance().withStyle(Style.EMPTY.withColor(CHERRY1)));
    }

    protected TextCache mixedChatMessage(MixedChatMessage message) {
        return TextCache.of(StyledString.concat(
                StyledString.fromString("<").fillColor(CHERRY6),
                this.rankName(message.sender()),
                StyledString.fromString("> ").fillColor(CHERRY6),
                message.content().mapStyle(this::whiteToCherry)
        ));
    }

    @Override
    public void onTabList(TabListCache tabList, Minecraft minecraft, ActionQueue queue) {
        for (TabEntryCache entry : tabList.entries) {
            this.onTabEntry(tabList, entry);
        }

        tabList.setHudColor(STEM_T);
        tabList.setEntryColor(CHERRY6_T);
    }

    protected void onTabEntry(TabListCache tabList, TabEntryCache entry) {
        switch (entry.result()) {
            case TabPing tabPing -> entry.setNameThemed(this.tabPing(tabPing));
            case TabPlayers tabPlayers -> entry.setNameThemed(this.tabPlayers(tabPlayers));
            case MainTabColumn mainTabColumn -> entry.setNameThemed(this.mainTabColumn(mainTabColumn));
            case TabLiteral.SERVERS -> entry.setNameThemed(LITERAL_SERVERS);
            case GameTabColumn gameTabColumn -> entry.setNameThemed(this.gameTabColumn(gameTabColumn));
            case GameTabSubserver gameTabSubserver -> entry.setNameThemed(this.gameTabSubserver(gameTabSubserver));
            default -> {}
        }
    }

    protected TextCache tabPing(TabPing ping) {
        return TextCache.of(StyledString.concat(
                TabPing.YOUR_PING_.fillColor(CHERRY5),
                StyledString.fromString(
                        " " + ping.latency() + "ms",
                        Style.EMPTY.withColor(DefaultTheme.colorLatencyLevel(LatencyLevel.fromLatency(ping.latency())))
                )
        ));
    }

    protected TextCache tabPlayers(TabPlayers players) {
        return TextCache.of(StyledString.concat(
                TabPlayers.PLAYERS_.fillColor(CHERRY5),
                StyledString.SPACE,
                StyledString.fromString(Integer.toString(players.playerCount()), Style.EMPTY.withColor(CHERRY6))
        ));
    }

    protected TextCache mainTabColumn(MainTabColumn column) {
        return TextCache.of(column.online() ? column.appearance().fillColor(CHERRY4) : column.appearance());
    }

    protected TextCache gameTabColumn(GameTabColumn gameTabColumn) {
        return TextCache.of(gameTabColumn.appearance().fillColor(CHERRY4));
    }

    protected TextCache gameTabSubserver(GameTabSubserver gameTabSubserver) {
        if (gameTabSubserver.playerCount() == -1 && gameTabSubserver.playerLimit() == -1) {
            return TextCache.of(StyledString.concat(
                    this.gameTabSubserverName(gameTabSubserver.subserverName()),
                    this.gameTabSubserverMode(gameTabSubserver.mode()),
                    gameTabSubserver.spaces(),
                    this.gameTabSubserverOffline()
            ));
        } else {
            TextColor countColor = gameTabSubserver.playerCount() == 0 ? CHERRY1 : CHERRY6;
            return TextCache.of(StyledString.concat(
                    this.gameTabSubserverName(gameTabSubserver.subserverName()),
                    this.gameTabSubserverMode(gameTabSubserver.mode()),
                    gameTabSubserver.spaces(),
                    StyledString.fromString(Integer.toString(gameTabSubserver.playerCount()), Style.EMPTY.withColor(countColor)),
                    StyledString.fromString("/", Style.EMPTY.withColor(CHERRY4)),
                    StyledString.fromString(Integer.toString(gameTabSubserver.playerLimit()), Style.EMPTY.withColor(CHERRY2))
            ));
        }
    }

    protected StyledString gameTabSubserverName(StyledString name) {
        return name;
    }

    protected StyledString gameTabSubserverMode(MinigameMode mode) {
        if (mode == MinigameMode.UNKNOWN) {
            return StyledString.fromString(":", Style.EMPTY.withColor(CHERRY5));
        } else {
            return StyledString.fromString(" (" + mode.text + "):", Style.EMPTY.withColor(CHERRY5));
        }
    }

    protected StyledString gameTabSubserverOffline() {
        return GameTabSubserver.OFFLINE.mapStyle(
                style -> style.withItalic(false)
                        .withColor(CHERRY1)
                        .withShadowColor(STEM_SHADOW)
        );
    }

    @Override
    public void onTabDeco(TabDecoCache tabDeco, Minecraft minecraft, ActionQueue queue) {
        tabDeco.setHeaderThemed(this.tabHeader(tabDeco.tabHeaderSoft()));
        tabDeco.setFooterThemed(this.tabFooter(tabDeco.tabFooterSoft()));
    }

    protected TextCache tabHeader(TabHeader tabHeader) {
        return TextCache.of(StyledString.concat(
                TabHeader.PREFIX1.fillColor(CHERRY1),
                TabHeader.INFIX1.fillColor(CHERRY5),
                this.rankName(tabHeader.rankName()),
                TabHeader.SUFFIX1.fillColor(CHERRY1),
                StyledString.NEWLINE,
                TabHeader.PREFIX2.fillColor(CHERRY5),
                this.cubeKrowd(tabHeader.cubeKrowd()),
                StyledString.NEWLINE,
                TabHeader.PREFIX3.fillColor(CHERRY4),
                tabHeader.time().appearance().fillColor(CHERRY6)
        ));
    }

    protected TextCache tabFooter(TabFooter tabFooter) {
        if (tabFooter.sections().length < 1) return TextCache.EMPTY;

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

    protected StyledString rankName(RankName rankName) {
        return rankName.appearance();
    }

    protected StyledString minigameTeamName(MinigameTeamName teamName) {
        return teamName.appearance();
    }

    protected StyledString cubeKrowd(StyledString cubeKrowd) {
        return cubeKrowd;
    }

    protected Style whiteToCherry(Style style) {
        return CKColor.WHITE.textColor.equals(style.getColor()) ? style.withColor(CHERRY6) : style;
    }
}
