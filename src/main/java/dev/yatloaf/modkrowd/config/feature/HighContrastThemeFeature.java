package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.config.PredicateIndex;
import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.cubekrowd.common.Afk;
import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.LatencyLevel;
import dev.yatloaf.modkrowd.cubekrowd.common.RankName;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.AfkMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.AlohaMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.MainChatMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.MessageCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MainTabName;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabPing;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MinigameTabName;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
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
    public void onMessage(MessageCache message, MinecraftClient client, ActionQueue queue) {
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
            case RESPECTED, VETERAN -> message.setThemed(TextCache.of(mainChatMessage.mapSender(this::modifyRankName).appearance()));
        }
    }

    @Override
    public void onTabList(TabListCache tabList, MinecraftClient client, ActionQueue queue) {
        for (TabEntryCache entry : tabList.result().entries()) {
            switch (entry.result()) {
                case TabPing tabPing -> entry.setThemed(this.tabPing(tabPing));
                case MainTabName mainTabName -> entry.setThemed(this.mainTabName(mainTabName));
                case MinigameTabName minigameTabName
                        when minigameTabName.afk() == Afk.TRUE -> entry.setThemed(this.minigameTabName(minigameTabName));
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

    protected TextCache minigameTabName(MinigameTabName minigameTabName) {
        return TextCache.of(StyledString.concat(
                this.afkStar(minigameTabName.afk()),
                minigameTabName.teamName().appearance()
        ));
    }

    protected StyledString afkStar(Afk afk) {
        return afk.star.fillColor(CKColor.LIGHT_PURPLE.textColor);
    }

    protected RankName modifyRankName(RankName rankName) {
        return switch (rankName.rank().letters()) {
            case RESPECTED -> rankName.mapName(name -> name.fillColor(CKColor.LIGHT_PURPLE.textColor));
            case VETERAN -> rankName.mapName(name -> name.fillColor(CKColor.YELLOW.textColor));
            default -> rankName;
        };
    }

}
