package dev.yatloaf.modkrowd.cubekrowd.tablist.cache;

import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCacheCache;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.GameLobbyTabList;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MainTabList;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MinigameTabList;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabList;
import dev.yatloaf.modkrowd.cubekrowd.tablist.UnknownTabList;
import dev.yatloaf.modkrowd.cubekrowd.tablist.VanillaTabList;
import dev.yatloaf.modkrowd.mixinduck.PlayerListHudDuck;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TabListCache {
    // Is this *really* necessary? Someone benchmark please
    private static final TextCacheCache CACHE = new TextCacheCache();

    private final PlayerListHud hud;
    private final PlayerListHudDuck hudDuck;

    private TabList result;

    private TabHeaderCache tabHeaderCache;
    private TabFooterCache tabFooterCache;

    private List<PlayerListEntry> playerListEntries;
    private List<TextCache> playerNames;

    // No nullable Integer to avoid indirection and allocation
    private long hudColor = 1L << 32;
    private long entryColor = 1L << 32;

    private TabListCache(MinecraftClient client) {
        this.hud = client.inGameHud.getPlayerListHud();
        this.hudDuck = (PlayerListHudDuck) this.hud;
    }

    public static TabListCache tryNew() {
        MinecraftClient client = MinecraftClient.getInstance();
        return client != null && client.player != null ? new TabListCache(client) : null;
    }

    public final TabList result() {
        if (this.result == null) {
            this.result = this.createResult();
        }
        return this.result;
    }

    protected TabList createResult() {
        VanillaTabList vanillaTabList = VanillaTabList.parseFast(this);
        if (vanillaTabList.isReal()) {
            return vanillaTabList;
        }

        MinigameTabList minigameTabList = MinigameTabList.parseFast(this);
        if (minigameTabList.isReal()) {
            return minigameTabList;
        }

        GameLobbyTabList gameLobbyTabList = GameLobbyTabList.parseFast(this);
        if (gameLobbyTabList.isReal()) {
            return gameLobbyTabList;
        }

        MainTabList mainTabList = MainTabList.parseFast(this);
        if (mainTabList.isReal()) {
            return mainTabList;
        }

        return UnknownTabList.parse(this);
    }

    public final TabHeaderCache tabHeaderCache() {
        if (this.tabHeaderCache == null) {
            this.tabHeaderCache = new TabHeaderCache(TextCache.of(Objects.requireNonNullElse(this.hudDuck.modKrowd$getHeader(), Text.empty())));
        }
        return this.tabHeaderCache;
    }

    public final TabFooterCache tabFooterCache() {
        if (this.tabFooterCache == null) {
            this.tabFooterCache = new TabFooterCache(TextCache.of(Objects.requireNonNullElse(this.hudDuck.modKrowd$getFooter(), Text.empty())));
        }
        return this.tabFooterCache;
    }

    public final List<PlayerListEntry> playerListEntries() {
        if (this.playerListEntries == null) {
            this.playerListEntries = this.hudDuck.modKrowd$collectEntries();
        }
        return this.playerListEntries;
    }

    public final TextCache getPlayerName(int index) {
        if (this.playerNames == null) {
            this.playerNames = Arrays.asList(new TextCache[this.playerListEntries().size()]);
        }
        if (this.playerNames.get(index) == null) {
            this.playerNames.set(index, CACHE.get((MutableText) this.hud.getPlayerName(this.playerListEntries().get(index))));
        }
        return this.playerNames.get(index);
    }

    public final void setHudColor(int color) {
        this.hudColor = Integer.toUnsignedLong(color);
    }

    public final int hudColorOr(int fallback) {
        return (this.hudColor & 1L << 32) == 0
                ? (int) this.hudColor
                : fallback;
    }

    public final void setEntryColor(int color) {
        this.entryColor = Integer.toUnsignedLong(color);
    }

    public final int entryColorOr(int fallback) {
        return (this.entryColor & 1L << 32) == 0
                ? (int) this.entryColor
                : fallback;
    }
}
