package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.config.DefaultTheme;
import dev.yatloaf.modkrowd.config.PredicateIndex;
import dev.yatloaf.modkrowd.cubekrowd.common.Afk;
import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.LatencyLevel;
import dev.yatloaf.modkrowd.cubekrowd.common.RankName;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.AfkMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.Aloha;
import dev.yatloaf.modkrowd.cubekrowd.message.AlohaMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.FishslapChatMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.MainChatMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.MissileWarsDeathMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.MessageCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MainTabName;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MinigameTabName;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabHeader;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabPing;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabHeaderCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import dev.yatloaf.modkrowd.util.text.StyledString;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

public class HighContrastThemeFeature extends ThemeFeature {
    public static final TextColor PINK = TextColor.fromRgb(0xFF9FCF);
    public static final TextColor LAVENDER = TextColor.fromRgb(0xBF7FFF);
    public static final TextColor CORNFLOWER = TextColor.fromRgb(0x5F7FFF);

    public HighContrastThemeFeature(String id, PredicateIndex allowedPredicates) {
        super(id, allowedPredicates);
    }

    @Override
    public void onMessage(MessageCache message, Minecraft minecraft, ActionQueue queue) {
        switch (message.result()) {
            case AlohaMessage alohaMessage -> this.onAlohaMessage(message, alohaMessage);
            case AfkMessage afkMessage -> this.onAfkMessage(message, afkMessage);
            case MainChatMessage mainChatMessage -> this.onMainChatMessage(message, mainChatMessage);
            case MissileWarsDeathMessage missileWarsDeathMessage -> this.onMissileWarsDeathMessage(message, missileWarsDeathMessage);
            case FishslapChatMessage fishslapChatMessage -> this.onFishslapChatMessage(message, fishslapChatMessage);
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
                    StyledString.concat(
                            StyledString.fromString("→ "),
                            afkMessage.name(),
                            StyledString.SPACE
                    ).fillColor(PINK),
                    Afk.FALSE.messageSuffix.fillColor(CKColor.GRAY.textColor)
            )));
            case TRUE -> message.setThemed(TextCache.of(StyledString.concat(
                    StyledString.concat(
                            StyledString.fromString("← "),
                            afkMessage.name(),
                            StyledString.SPACE
                    ).fillColor(PINK),
                    Afk.TRUE.messageSuffix.fillColor(CKColor.GRAY.textColor)
            )));
        }
    }

    protected void onMainChatMessage(MessageCache message, MainChatMessage mainChatMessage) {
        switch (mainChatMessage.sender().rank().letters()) {
            case RESPECTED, VETERAN, DEVELOPER -> message.setThemed(TextCache.of(mainChatMessage.mapSender(this::modifyRankName).appearance()));
        }
    }

    protected void onMissileWarsDeathMessage(MessageCache message, MissileWarsDeathMessage missileWarsDeathMessage) {
        // The args should all already have a non-null color
        message.setThemed(TextCache.of(missileWarsDeathMessage.appearance().withStyle(CKColor.GRAY.style)));
    }

    protected void onFishslapChatMessage(MessageCache message, FishslapChatMessage fishslapChatMessage) {
        switch (fishslapChatMessage.sender().rank().letters()) {
            case RESPECTED, VETERAN, DEVELOPER -> message.setThemed(TextCache.of(fishslapChatMessage.mapSender(this::modifyRankName).appearance()));
        }
    }

    @Override
    public void onTabList(TabListCache tabList, Minecraft minecraft, ActionQueue queue) {
        for (TabEntryCache entry : tabList.result().entries()) {
            switch (entry.result()) {
                case TabPing tabPing -> entry.setThemed(this.tabPing(tabPing));
                case MainTabName mainTabName -> entry.setThemed(this.mainTabName(mainTabName));
                case MinigameTabName minigameTabName
                        when minigameTabName.afk() == Afk.TRUE -> entry.setThemed(this.minigameTabName(minigameTabName));
                default -> {}
            }
        }
        TabHeaderCache tabHeader = tabList.tabHeaderCache();
        switch (tabHeader.tabHeaderSoft().rankName().rank().letters()) {
            case RESPECTED, VETERAN -> tabHeader.setThemed(this.tabHeader(tabHeader.tabHeaderSoft()));
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

    protected TextCache minigameTabName(MinigameTabName minigameTabName) {
        return TextCache.of(StyledString.concat(
                this.afkStar(minigameTabName.afk()),
                minigameTabName.teamName().appearance()
        ));
    }

    protected TextCache tabHeader(TabHeader tabHeader) {
        return TextCache.of(StyledString.concat(
                TabHeader.PREFIX1,
                TabHeader.INFIX1,
                this.modifyRankName(tabHeader.rankName()).appearance(),
                TabHeader.SUFFIX1,
                StyledString.NEWLINE,
                TabHeader.PREFIX2,
                tabHeader.time().appearance()
        ));
    }

    protected StyledString afkStar(Afk afk) {
        return afk.star.fillColor(PINK);
    }

    protected RankName modifyRankName(RankName rankName) {
        return switch (rankName.rank().letters()) {
            case RESPECTED -> rankName.mapName(name -> name.fillColor(PINK));
            case VETERAN -> rankName.mapName(name -> name.fillColor(LAVENDER));
            case DEVELOPER -> rankName.mapName(name -> name.fillColor(CORNFLOWER));
            default -> rankName;
        };
    }

}
