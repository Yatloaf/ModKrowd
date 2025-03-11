package dev.yatloaf.modkrowd.config;

import net.minecraft.client.MinecraftClient;

public class ActionQueue {
    private boolean reloadResources = false;
    private boolean reloadChunks = false;

    public void queueReloadResources() {
        this.reloadResources = true;
    }

    public void queueReloadChunks() {
        this.reloadChunks = true;
    }

    public void flush(MinecraftClient client) {
        if (this.reloadResources) {
            client.reloadResources();
            this.reloadResources = false;
            this.reloadChunks = false;
        } else if (this.reloadChunks) {
            client.worldRenderer.reload();
            this.reloadChunks = false;
        }
    }
}
