package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.config.DefaultTheme;
import dev.yatloaf.modkrowd.config.Restriction;
import dev.yatloaf.modkrowd.cubekrowd.common.Afk;
import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.LatencyLevel;
import dev.yatloaf.modkrowd.cubekrowd.common.RankName;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.AfkMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.Aloha;
import dev.yatloaf.modkrowd.cubekrowd.message.AlohaMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.MixedChatMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.RankChatMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.DeathMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.MessageCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.GameTabSubserver;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MainTabName;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MinigameMode;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MinigameTabName;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabDecoCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabHeader;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabPing;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import dev.yatloaf.modkrowd.util.text.StyledString;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.ARGB;

public class HighContrastThemeFeature extends ThemeFeature {
    public static final TextColor PINK = TextColor.fromRgb(0xFF9FCF);
    public static final TextColor CORNFLOWER = TextColor.fromRgb(0x5F7FFF);

    public static final int OFFLINE_SHADOW = 0xFF_00_00_00 | ARGB.scaleRGB(CKColor.RED.textColor.getValue(), 0.25F);

    public HighContrastThemeFeature(String id, Restriction restriction) {
        super(id, restriction);
    }

    @Override
    public void onMessage(MessageCache message, Minecraft minecraft, ActionQueue queue) {
        switch (message.result()) {
            case AlohaMessage alohaMessage -> this.onAlohaMessage(message, alohaMessage);
            case AfkMessage afkMessage -> this.onAfkMessage(message, afkMessage);
            case RankChatMessage rankChatMessage -> this.onRankChatMessage(message, rankChatMessage);
            case DeathMessage deathMessage -> this.onDeathMessage(message, deathMessage);
            case MixedChatMessage mixedChatMessage -> this.onMixedChatMessage(message, mixedChatMessage);
            default -> {}
        }
    }

    protected void onAlohaMessage(MessageCache message, AlohaMessage alohaMessage) {
        switch (alohaMessage.aloha()) {
            case JOIN -> message.setThemed(TextCache.of(
                    Component.literal("+ ").setStyle(CKColor.GREEN.style).append(Component.translatable(
                            Aloha.JOIN.key,
                            alohaMessage.name().fillColor(CKColor.YELLOW.textColor).toText()
                    ).setStyle(CKColor.GRAY.style))
            ));
            case LEAVE -> message.setThemed(TextCache.of(
                    Component.literal("- ").setStyle(CKColor.RED.style).append(Component.translatable(
                            Aloha.LEAVE.key,
                            alohaMessage.name().fillColor(CKColor.YELLOW.textColor).toText()
                    ).setStyle(CKColor.GRAY.style))
            ));
        }
    }

    protected void onAfkMessage(MessageCache message, AfkMessage afkMessage) {
        switch (afkMessage.afk()) {
            case FALSE -> message.setThemed(TextCache.of(StyledString.concat(
                    StyledString.fromString("→ ", CKColor.GREEN.style),
                    afkMessage.name().fillColor(PINK),
                    StyledString.SPACE,
                    Afk.FALSE.messageSuffix.fillColor(CKColor.GRAY.textColor)
            )));
            case TRUE -> message.setThemed(TextCache.of(StyledString.concat(
                    StyledString.fromString("← ", CKColor.RED.style),
                    afkMessage.name().fillColor(PINK),
                    StyledString.SPACE,
                    Afk.TRUE.messageSuffix.fillColor(CKColor.GRAY.textColor)
            )));
        }
    }

    protected void onRankChatMessage(MessageCache message, RankChatMessage rankChatMessage) {
        switch (rankChatMessage.sender().rank().letters()) {
            case RESPECTED, VETERAN, DEVELOPER -> message.setThemed(TextCache.of(rankChatMessage.mapSender(this::modifyRankName).appearance()));
        }
    }

    protected void onDeathMessage(MessageCache message, DeathMessage deathMessage) {
        // The args should all already have a non-null color
        message.setThemed(TextCache.of(deathMessage.appearance().withStyle(CKColor.GRAY.style)));
    }

    protected void onMixedChatMessage(MessageCache message, MixedChatMessage mixedChatMessage) {
        switch (mixedChatMessage.sender().rank().letters()) {
            case RESPECTED, VETERAN, DEVELOPER -> message.setThemed(TextCache.of(mixedChatMessage.mapSender(this::modifyRankName).appearance()));
        }
    }

    @Override
    public void onTabList(TabListCache tabList, Minecraft minecraft, ActionQueue queue) {
        for (TabEntryCache entry : tabList.entries) {
            switch (entry.result()) {
                case TabPing tabPing -> entry.setNameThemed(this.tabPing(tabPing));
                case MainTabName mainTabName -> entry.setNameThemed(this.mainTabName(mainTabName));
                case GameTabSubserver gameTabSubserver -> entry.setNameThemed(this.gameTabSubserver(gameTabSubserver));
                case MinigameTabName minigameTabName
                        when minigameTabName.afk() == Afk.TRUE -> entry.setNameThemed(this.minigameTabName(minigameTabName));
                default -> {}
            }
        }
    }

    protected TextCache tabPing(TabPing tabPing) {
        return TextCache.of(StyledString.concat(
                TabPing.YOUR_PING_,
                StyledString.fromString(
                        " " + tabPing.latency() + "ms",
                        Style.EMPTY.withColor(DefaultTheme.colorLatencyLevel(LatencyLevel.fromLatency(tabPing.latency())))
                )
        ));
    }

    protected TextCache mainTabName(MainTabName mainTabName) {
        return TextCache.of(StyledString.concat(
                this.afkStar(mainTabName.afk()),
                this.modifyRankName(mainTabName.rankName()).appearance()
        ));
    }

    protected TextCache gameTabSubserver(GameTabSubserver gameTabSubserver) {
        if (gameTabSubserver.playerCount() == -1 && gameTabSubserver.playerLimit() == -1) {
            return TextCache.of(StyledString.concat(
                    gameTabSubserver.subserverName(),
                    this.gameTabSubserverMode(gameTabSubserver.mode()),
                    gameTabSubserver.spaces(),
                    this.gameTabSubserverOffline()
            ));
        } else {
            CKColor countColor = gameTabSubserver.playerCount() == 0 ? CKColor.SLEET : CKColor.WHITE;
            return TextCache.of(StyledString.concat(
                    gameTabSubserver.subserverName(),
                    this.gameTabSubserverMode(gameTabSubserver.mode()),
                    gameTabSubserver.spaces(),
                    StyledString.fromString(Integer.toString(gameTabSubserver.playerCount()), countColor.style),
                    StyledString.fromString("/", CKColor.GRAY.style),
                    StyledString.fromString(Integer.toString(gameTabSubserver.playerLimit()), CKColor.RED.style)
            ));
        }
    }

    protected StyledString gameTabSubserverMode(MinigameMode mode) {
        if (mode == MinigameMode.UNKNOWN) {
            return StyledString.fromString(":", CKColor.GRAY.style);
        } else {
            return StyledString.fromString(" (" + mode.text + "):", CKColor.GRAY.style);
        }
    }

    protected StyledString gameTabSubserverOffline() {
        return GameTabSubserver.OFFLINE.mapStyle(
                style -> style.withItalic(false)
                        .withShadowColor(OFFLINE_SHADOW)
        );
    }

    protected TextCache minigameTabName(MinigameTabName minigameTabName) {
        return TextCache.of(StyledString.concat(
                this.afkStar(minigameTabName.afk()),
                minigameTabName.teamName().appearance()
        ));
    }

    @Override
    public void onTabDeco(TabDecoCache tabDeco, Minecraft minecraft, ActionQueue queue) {
        TabHeader tabHeader = tabDeco.tabHeaderSoft();
        switch (tabHeader.rankName().rank().letters()) {
            case RESPECTED, VETERAN -> tabDeco.setHeaderThemed(this.tabHeader(tabHeader));
        }
    }

    protected TextCache tabHeader(TabHeader tabHeader) {
        return TextCache.of(StyledString.concat(
                TabHeader.PREFIX1,
                TabHeader.INFIX1,
                this.modifyRankName(tabHeader.rankName()).appearance(),
                TabHeader.SUFFIX1,
                StyledString.NEWLINE,
                TabHeader.PREFIX3,
                tabHeader.time().appearance()
        ));
    }

    protected StyledString afkStar(Afk afk) {
        return afk.star.fillColor(PINK);
    }

    protected RankName modifyRankName(RankName rankName) {
        return switch (rankName.rank().letters()) {
            case RESPECTED -> rankName.mapName(name -> name.fillColor(PINK));
            case VETERAN -> rankName.mapName(name -> name.fillColor(CKColor.LAVENDER.textColor));
            case DEVELOPER -> rankName.mapName(name -> name.fillColor(CORNFLOWER));
            default -> rankName;
        };
    }

}
