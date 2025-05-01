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
import dev.yatloaf.modkrowd.cubekrowd.message.FishslapChatMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.MainChatMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.MessageCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MainTabName;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabHeader;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabPing;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MinigameTabName;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabHeaderCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import dev.yatloaf.modkrowd.config.DefaultTheme;
import dev.yatloaf.modkrowd.util.text.StyledString;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;

public class HighContrastThemeFeature extends ThemeFeature {
    public static final TextColor PINK = TextColor.fromRgb(0xFF9FCF);
    public static final TextColor LAVENDER = TextColor.fromRgb(0xBF7FFF);

    public HighContrastThemeFeature(String id, PredicateIndex allowedPredicates) {
        super(id, allowedPredicates);
    }

    @Override
    public void onMessage(MessageCache message, MinecraftClient client, ActionQueue queue) {
        switch (message.result()) {
            case AlohaMessage alohaMessage -> this.onAlohaMessage(message, alohaMessage);
            case AfkMessage afkMessage -> this.onAfkMessage(message, afkMessage);
            case MainChatMessage mainChatMessage -> this.onMainChatMessage(message, mainChatMessage);
            case FishslapChatMessage fishslapChatMessage -> this.onFishslapChatMessage(message, fishslapChatMessage);
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

    protected void onFishslapChatMessage(MessageCache message, FishslapChatMessage fishslapChatMessage) {
        switch (fishslapChatMessage.sender().rank().letters()) {
            case RESPECTED, VETERAN -> message.setThemed(TextCache.of(fishslapChatMessage.mapSender(this::modifyRankName).appearance()));
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
            default -> rankName;
        };
    }

}
