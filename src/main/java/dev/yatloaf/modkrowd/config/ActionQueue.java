package dev.yatloaf.modkrowd.config;

import net.minecraft.client.Minecraft;

public class ActionQueue {
    private boolean reloadResources = false;
    private boolean reloadChunks = false;

    public void queueReloadResources() {
        this.reloadResources = true;
    }

    public void queueReloadChunks() {
        this.reloadChunks = true;
    }

    public void flush(Minecraft minecraft) {
        if (this.reloadResources) {
            minecraft.reloadResourcePacks();
            this.reloadResources = false;
            this.reloadChunks = false;
        } else if (this.reloadChunks) {
            minecraft.levelRenderer.allChanged();
            this.reloadChunks = false;
        }
    }
}
