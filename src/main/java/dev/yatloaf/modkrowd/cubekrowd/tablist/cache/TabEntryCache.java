package dev.yatloaf.modkrowd.cubekrowd.tablist.cache;

import dev.yatloaf.modkrowd.config.DefaultTheme;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.ThemedCache;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabEntry;

public abstract class TabEntryCache extends ThemedCache {
    public final int latency;

    private TextCache latencyDefault;
    private TextCache latencyThemed;

    public TabEntryCache(TextCache original, int latency) {
        super(original);
        this.latency = latency;
    }

    public void setLatencyThemed(TextCache value) {
        this.latencyThemed = value;
    }

    public TextCache latencyThemedOrDefault() {
        return this.latencyThemed != null ? this.latencyThemed : this.latencyDefault();
    }

    public TextCache latencyDefault() {
        if (this.latencyDefault == null) {
            this.latencyDefault = DefaultTheme.formatLatency(this.latency);
        }
        return this.latencyDefault;
    }

    public abstract TabEntry result();
    public abstract boolean isPlayer();
    public abstract Subserver subserver();
}
