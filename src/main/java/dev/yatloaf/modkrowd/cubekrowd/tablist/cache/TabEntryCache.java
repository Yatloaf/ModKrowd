package dev.yatloaf.modkrowd.cubekrowd.tablist.cache;

import dev.yatloaf.modkrowd.config.DefaultTheme;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCacheCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabEntry;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.MutableComponent;

public class TabEntryCache {
    // Is this *really* necessary? Someone benchmark please
    private static final TextCacheCache CACHE = new TextCacheCache();

    public final int index;
    public final PlayerInfo info;
    public final String profileName;
    public final int latency;

    private final TabListCache parent;

    private TextCache name;

    private TextCache nameThemed;
    private TextCache latencyThemed;

    public TabEntryCache(TabListCache parent, int index, PlayerInfo info) {
        this.parent = parent;
        this.index = index;
        this.info = info;
        this.profileName = info.getProfile().name();
        this.latency = info.getLatency();
    }

    public TextCache name() {
        if (this.name == null) {
            if (this.parent.hud == null) {
                return TextCache.EMPTY;
            }
            MutableComponent nameComponent = (MutableComponent) this.parent.hud.getNameForDisplay(this.info);
            this.name = CACHE.get(nameComponent);
        }
        return this.name;
    }

    public TabEntry result() {
        return this.parent.result().entries()[this.index];
    }

    public void setNameThemed(TextCache themed) {
        this.nameThemed = themed;
    }

    public TextCache getNameThemed() {
        return this.nameThemed != null ? this.nameThemed : this.name();
    }

    public void setLatencyThemed(TextCache themed) {
        this.latencyThemed = themed;
    }

    public TextCache getLatencyThemed() {
        return this.latencyThemed != null ? this.latencyThemed : DefaultTheme.formatLatency(this.latency);
    }
}
