package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.config.Restriction;
import dev.yatloaf.modkrowd.cubekrowd.tablist.TabIcon;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class BlankTabIconsThemeFeature extends ThemeFeature {
    public static final ResourceLocation BLANK = ResourceLocation.fromNamespaceAndPath("modkrowd", "textures/theme/blank_tab_list_icon/blank.png");

    public BlankTabIconsThemeFeature(String id, Restriction restriction) {
        super(id, restriction);
    }

    @Override
    public void onTabList(TabListCache tabList, Minecraft minecraft, ActionQueue queue) {
        for (TabEntryCache entry : tabList.entries) {
            if (entry.icon == TabIcon.DARK_GRAY) {
                entry.setSkinThemed(BLANK);
            }
        }
    }
}
