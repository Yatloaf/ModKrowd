package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.config.DefaultTheme;
import dev.yatloaf.modkrowd.config.PredicateIndex;
import dev.yatloaf.modkrowd.config.queue.ActionQueue;
import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.CKStuff;
import dev.yatloaf.modkrowd.cubekrowd.common.LatencyLevel;
import dev.yatloaf.modkrowd.cubekrowd.common.MinigameTeamName;
import dev.yatloaf.modkrowd.cubekrowd.common.RankName;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.AfkMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.AlohaMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.DirectMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.MainChatMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.MinigameChatMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.MessageCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MainTabColumn;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MainTabPing;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MainTabPlayers;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabFooter;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabFooterSection;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabHeader;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabFooterCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabHeaderCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import dev.yatloaf.modkrowd.util.text.StyledString;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;

public class CherryLiteThemeFeature extends ThemeFeature {
    public static final TextColor CHERRY1 = TextColor.fromRgb(0xe986bb);
    public static final TextColor CHERRY2 = TextColor.fromRgb(0xec95bf);
    public static final TextColor CHERRY3 = TextColor.fromRgb(0xefa7cd);
    public static final TextColor CHERRY4 = TextColor.fromRgb(0xf7b9dc);
    public static final TextColor CHERRY5 = TextColor.fromRgb(0xfccbe7);
    public static final TextColor CHERRY6 = TextColor.fromRgb(0xf5daef);

    public CherryLiteThemeFeature(String id, PredicateIndex allowedPredicates) {
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
                StyledString.fromString("<").fillColor(CHERRY6),
                this.minigameTeamName(message.teamName()),
                StyledString.fromString("> ").fillColor(CHERRY6),
                message.content().mapStyle(this::whiteToCherry)
        ));
    }

    @Override
    public void onTabList(TabListCache tabList, MinecraftClient client, ActionQueue queue) {
        for (TabEntryCache entry : tabList.result().entries()) {
            this.onTabEntry(entry);
        }
        TabHeaderCache tabHeader = tabList.tabHeaderCache();
        TabFooterCache tabFooter = tabList.tabFooterCache();
        tabHeader.setThemed(this.tabHeader(tabHeader.tabHeaderSoft()));
        tabFooter.setThemed(this.tabFooter(tabFooter.tabFooterSoft()));
    }

    protected void onTabEntry(TabEntryCache entry) {
        switch (entry.result()) {
            case MainTabColumn mainTabColumn -> entry.setThemed(this.mainTabColumn(mainTabColumn));
            case MainTabPlayers mainTabPlayers -> entry.setThemed(this.mainTabPlayers(mainTabPlayers));
            case MainTabPing mainTabPing -> entry.setThemed(this.mainTabPing(mainTabPing));
            default -> {}
        }
    }

    protected TextCache mainTabColumn(MainTabColumn column) {
        return TextCache.of(column.online() ? column.appearance().fillColor(CHERRY4) : column.appearance());
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
                StyledString.fromString(
                        " " + ping.latency() + "ms",
                        Style.EMPTY.withColor(DefaultTheme.colorLatencyLevel(LatencyLevel.fromLatency(ping.latency())))
                )
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

    protected Style whiteToCherry(Style style) {
        return CKColor.WHITE.textColor.equals(style.getColor()) ? style.withColor(CHERRY6) : style;
    }
}
