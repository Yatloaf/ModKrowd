package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.config.PredicateIndex;
import net.minecraft.client.MinecraftClient;

public class ReloadResourcesFeature extends Feature {
    public ReloadResourcesFeature(String id, PredicateIndex allowedPredicates) {
        super(id, allowedPredicates);
    }

    @Override
    public void onEnable(MinecraftClient client, ActionQueue queue) {
        queue.queueReloadResources();
    }

    @Override
    public void onDisable(MinecraftClient client, ActionQueue queue) {
        queue.queueReloadResources();
    }
}
