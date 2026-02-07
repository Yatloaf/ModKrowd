package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.config.Restriction;
import net.minecraft.client.Minecraft;

public class ThemeFeature extends Feature {
    public ThemeFeature(String id, Restriction restriction) {
        super(id, restriction);
    }

    @Override
    public void onEnable(Minecraft minecraft, ActionQueue queue) {
        ModKrowd.TAB_LIST.invalidateThemed();
        ModKrowd.TAB_DECO.invalidateThemed();
    }

    @Override
    public void onDisable(Minecraft minecraft, ActionQueue queue) {
        ModKrowd.TAB_LIST.invalidateThemed();
        ModKrowd.TAB_DECO.invalidateThemed();
    }
}
