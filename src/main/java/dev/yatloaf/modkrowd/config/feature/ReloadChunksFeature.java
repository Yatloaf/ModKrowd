package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.config.Restriction;
import net.minecraft.client.Minecraft;

public class ReloadChunksFeature extends Feature {
    public ReloadChunksFeature(String id, Restriction restriction) {
        super(id, restriction);
    }

    @Override
    public void onEnable(Minecraft minecraft, ActionQueue queue) {
        queue.queueReloadChunks();
    }

    @Override
    public void onDisable(Minecraft minecraft, ActionQueue queue) {
        queue.queueReloadChunks();
    }
}
