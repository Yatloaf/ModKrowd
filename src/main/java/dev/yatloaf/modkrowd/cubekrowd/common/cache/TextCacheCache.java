package dev.yatloaf.modkrowd.cubekrowd.common.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.network.chat.MutableComponent;

/**
 * The epitome of human achievement.
 */
public class TextCacheCache {
    // Assuming the texts never get mutated...
    private final LoadingCache<MutableComponent, TextCache> cache = CacheBuilder.newBuilder()
            .weakKeys()
            .maximumSize(160)
            .build(CacheLoader.from(TextCache::of));

    public TextCache get(MutableComponent text) {
        return this.cache.getUnchecked(text);
    }
}
