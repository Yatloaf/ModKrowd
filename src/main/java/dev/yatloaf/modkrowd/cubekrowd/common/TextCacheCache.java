package dev.yatloaf.modkrowd.cubekrowd.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

/**
 * The epitome of human achievement.
 */
public class TextCacheCache {
    // Assuming the texts never get mutated...
    private final LoadingCache<@NotNull MutableComponent, @NotNull TextCache> cache = CacheBuilder.newBuilder()
            .weakKeys()
            .maximumSize(160)
            .build(CacheLoader.from(TextCache::of));

    public TextCache get(MutableComponent text) {
        return this.cache.getUnchecked(text);
    }
}
