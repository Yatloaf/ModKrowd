package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.config.PredicateIndex;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import net.minecraft.client.MinecraftClient;

public class ShadedTabThemeFeature extends ThemeFeature {
    public static final int BLACK75 = 0xC0000000;

    public ShadedTabThemeFeature(String id, PredicateIndex allowedPredicates) {
        super(id, allowedPredicates);
    }

    @Override
    public void onTabList(TabListCache tabList, MinecraftClient client, ActionQueue queue) {
        tabList.setHudColor(BLACK75);
    }
}
