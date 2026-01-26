package dev.yatloaf.modkrowd.cubekrowd.tablist.cache;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.cubekrowd.tablist.MainTabList;
import dev.yatloaf.modkrowd.cubekrowd.tablist.GameTabList;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabList;
import dev.yatloaf.modkrowd.cubekrowd.tablist.VanillaTabList;
import dev.yatloaf.modkrowd.mixinduck.PlayerTabOverlayDuck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TabListCache {
    public @Nullable PlayerTabOverlay hud;
    public List<PlayerInfo> playerInfos;
    public TabEntryCache[] entries;

    private TabList result;

    // No nullable Integer to avoid indirection and allocation
    private boolean hudColorSet = false;
    private int hudColor;
    private boolean entryColorSet = false;
    private int entryColor;

    public TabListCache() {
        this.invalidateAll();
    }

    @SuppressWarnings("ConstantValue")
    public void invalidateAll() {
        Minecraft minecraft = Minecraft.getInstance();
        // This can happen due to circular class loading
        if (minecraft == null) {
            this.hud = null;
            this.playerInfos = List.of();
        } else {
            this.hud = minecraft.gui.getTabList();
            this.playerInfos = ((PlayerTabOverlayDuck) this.hud).modKrowd$getPlayerInfos();
        }

        this.result = null;

        this.invalidateThemed();
    }

    public void invalidateThemed() {
        this.entries = new TabEntryCache[this.playerInfos.size()];
        for (int index = 0; index < this.entries.length; index++) {
            this.entries[index] = new TabEntryCache(this, index, this.playerInfos.get(index));
        }

        this.hudColorSet = false;
        this.entryColorSet = false;

        ModKrowd.CONFIG.onTabList(this);
    }

    public TabList result() {
        if (this.result == null) {
            this.result = this.createResult();
        }
        return this.result;
    }

    private TabList createResult() {
        VanillaTabList vanillaTabList = VanillaTabList.parseFast(this);
        if (vanillaTabList.isReal()) return vanillaTabList;

        GameTabList gameTabList = GameTabList.parseFast(this);
        if (gameTabList.isReal()) return gameTabList;

        MainTabList mainTabList = MainTabList.parseFast(this);
        if (mainTabList.isReal()) return mainTabList;

        return VanillaTabList.parseSoft(this);
    }

    public void setHudColor(int color) {
        this.hudColor = color;
        this.hudColorSet = true;
    }

    public int hudColorOr(int fallback) {
        return this.hudColorSet ? this.hudColor : fallback;
    }

    public void setEntryColor(int color) {
        this.entryColor = color;
        this.entryColorSet = true;
    }

    public int entryColorOr(int fallback) {
        return this.entryColorSet ? this.entryColor : fallback;
    }
}
