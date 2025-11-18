package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.config.PredicateIndex;
import net.minecraft.client.Minecraft;

public class ReloadResourcesFeature extends Feature {
    public ReloadResourcesFeature(String id, PredicateIndex allowedPredicates) {
        super(id, allowedPredicates);
    }

    @Override
    public void onEnable(Minecraft minecraft, ActionQueue queue) {
        queue.queueReloadResources();
    }

    @Override
    public void onDisable(Minecraft minecraft, ActionQueue queue) {
        queue.queueReloadResources();
    }
}
