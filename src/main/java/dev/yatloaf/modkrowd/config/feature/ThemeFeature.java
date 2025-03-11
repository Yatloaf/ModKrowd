package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.config.PredicateIndex;
import dev.yatloaf.modkrowd.config.ActionQueue;
import net.minecraft.client.MinecraftClient;

public class ThemeFeature extends Feature {
    public ThemeFeature(String id, PredicateIndex allowedPredicates) {
        super(id, allowedPredicates);
    }

    @Override
    public void onEnable(MinecraftClient client, ActionQueue queue) {
        ModKrowd.invalidateTabListCache();
    }
}
