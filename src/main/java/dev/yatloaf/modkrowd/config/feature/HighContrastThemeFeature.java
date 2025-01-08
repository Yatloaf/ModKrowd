package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.config.PredicateIndex;
import dev.yatloaf.modkrowd.config.queue.ActionQueue;
import dev.yatloaf.modkrowd.cubekrowd.common.Afk;
import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.LatencyLevel;
import dev.yatloaf.modkrowd.cubekrowd.common.RankName;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.AfkMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.AlohaMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.MainChatMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.MessageCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MainTabList;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MainTabName;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MainTabPing;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MinigameTabList;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MinigameTabName;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import dev.yatloaf.modkrowd.config.DefaultTheme;
import dev.yatloaf.modkrowd.util.text.StyledString;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;

public class HighContrastThemeFeature extends ThemeFeature {
    public HighContrastThemeFeature(String id, PredicateIndex allowedPredicates) {
        super(id, allowedPredicates);
    }

    @Override
    public void onMessage(MessageCache message, boolean overlay, MinecraftClient client, ActionQueue queue) {
        switch (message.result()) {
            case AlohaMessage alohaMessage -> this.onAlohaMessage(message, alohaMessage);
            case AfkMessage afkMessage -> this.onAfkMessage(message, afkMessage);
            case MainChatMessage mainChatMessage -> this.onMainChatMessage(message, mainChatMessage);
            default -> {}
        }
    }

    protected void onAlohaMessage(MessageCache message, AlohaMessage alohaMessage) {
        switch (alohaMessage.aloha()) {
            case JOIN -> message.setThemed(TextCache.of(StyledString.concat(
                    StyledString.fromString("+ ", CKColor.DARK_GREEN.style),
                    alohaMessage.name().fillColor(CKColor.WHITE.textColor),
                    StyledString.fromString(" joined", CKColor.GRAY.style)
            )));
            case LEAVE -> message.setThemed(TextCache.of(StyledString.concat(
                    StyledString.fromString("- ", CKColor.DARK_RED.style),
                    alohaMessage.name().fillColor(CKColor.WHITE.textColor),
                    StyledString.fromString(" left", CKColor.GRAY.style)
            )));
        }
    }

    protected void onAfkMessage(MessageCache message, AfkMessage afkMessage) {
        switch (afkMessage.afk()) {
            case FALSE -> message.setThemed(TextCache.of(StyledString.concat(
                    StyledString.fromString("+ ", CKColor.GREEN.style),
                    afkMessage.name().fillColor(CKColor.WHITE.textColor),
                    StyledString.fromString(" is no longer AFK", CKColor.GRAY.style)
            )));
            case TRUE -> message.setThemed(TextCache.of(StyledString.concat(
                    StyledString.fromString("- ", CKColor.RED.style),
                    afkMessage.name().fillColor(CKColor.WHITE.textColor),
                    StyledString.fromString(" is now AFK", CKColor.GRAY.style)
            )));
        }
    }

    protected void onMainChatMessage(MessageCache message, MainChatMessage mainChatMessage) {
        switch (mainChatMessage.sender().rank().letters()) {
            case RESPECTED, VETERAN -> message.setThemed(TextCache.of(mainChatMessage.mapSender(HighContrastThemeFeature::modifyRankName).appearance()));
        }
    }

    @Override
    public void onTabList(TabListCache tabList, MinecraftClient client, ActionQueue queue) {
        switch (tabList.result()) {
            case MainTabList mainTabList -> this.onMainTabList(mainTabList);
            case MinigameTabList minigameTabList -> this.onMinigameTabList(minigameTabList);
            default -> {}
        }
    }

    protected void onMainTabList(MainTabList mainTabList) {
        for (MainTabList.EntryCache entry : mainTabList.entries()) {
            switch (entry.result()) {
                case MainTabName mainTabName -> entry.setThemed(TextCache.of(StyledString.concat(
                        mainTabName.afk().star.fillColor(CKColor.LIGHT_PURPLE.textColor),
                        modifyRankName(mainTabName.rankName()).appearance()
                )));
                case MainTabPing mainTabPing -> entry.setThemed(TextCache.of(StyledString.concat(
                        MainTabPing.YOUR_PING_,
                        StyledString.fromString(
                                " " + mainTabPing.latency() + "ms",
                                Style.EMPTY.withColor(DefaultTheme.colorLatencyLevel(LatencyLevel.fromLatency(mainTabPing.latency())))
                        )
                )));
                default -> {}
            }
        }
    }

    protected void onMinigameTabList(MinigameTabList minigameTabList) {
        for (MinigameTabList.EntryCache player : minigameTabList.players()) {
            if (player.result() instanceof MinigameTabName minigameTabName && minigameTabName.afk() == Afk.TRUE) {
                player.setThemed(TextCache.of(minigameTabName.mapTeamName(teamName -> teamName.mapName(StyledString::fillStrikethrough)).appearance()));
            }
        }
    }

    protected static RankName modifyRankName(RankName rankName) {
        return switch (rankName.rank().letters()) {
            case RESPECTED -> rankName.mapName(name -> name.fillColor(CKColor.LIGHT_PURPLE.textColor));
            case VETERAN -> rankName.mapName(name -> name.fillColor(CKColor.YELLOW.textColor));
            default -> rankName;
        };
    }

}
