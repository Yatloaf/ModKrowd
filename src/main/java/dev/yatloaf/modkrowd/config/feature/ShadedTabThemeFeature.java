package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.config.Restriction;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import net.minecraft.client.Minecraft;

public class ShadedTabThemeFeature extends ThemeFeature {
    public static final int BLACK75 = 0xC0000000;

    public ShadedTabThemeFeature(String id, Restriction restriction) {
        super(id, restriction);
    }

    @Override
    public void onTabList(TabListCache tabList, Minecraft minecraft, ActionQueue queue) {
        tabList.setHudColor(BLACK75);
    }
}
