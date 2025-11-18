package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.config.PredicateIndex;
import net.minecraft.client.Minecraft;

public class ThemeFeature extends Feature {
    public ThemeFeature(String id, PredicateIndex allowedPredicates) {
        super(id, allowedPredicates);
    }

    @Override
    public void onEnable(Minecraft minecraft, ActionQueue queue) {
        ModKrowd.invalidateTabListCache();
    }

    @Override
    public void onDisable(Minecraft minecraft, ActionQueue queue) {
        ModKrowd.invalidateTabListCache();
    }
}
