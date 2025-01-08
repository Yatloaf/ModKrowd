package dev.yatloaf.modkrowd.cubekrowd.common.cache;

public class ThemedCache {
    public final TextCache original;

    private TextCache themed;

    public ThemedCache(TextCache original) {
        this.original = original;
    }

    public final TextCache themedOrNull() {
        return this.themed;
    }

    public final TextCache themedOrDefault() {
        return this.themed != null ? this.themed : this.original;
    }

    public void setThemed(TextCache value) {
        this.themed = value;
    }
}
