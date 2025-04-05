package dev.yatloaf.modkrowd.config;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.config.exception.ConfigException;
import dev.yatloaf.modkrowd.config.feature.Feature;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.MessageCache;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import dev.yatloaf.modkrowd.custom.Custom;
import dev.yatloaf.modkrowd.mixin.EntityAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientConfigurationNetworkHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SyncedConfig extends Config {
    public final List<Feature> enabledFeatures = new ArrayList<>(FEATURE_ESTIMATION);

    private boolean dirty = true;

    public void tryDeserialize(File file) {
        try {
            this.deserialize(file);
            this.dirty = false;
        } catch (ConfigException e) {
            ModKrowd.LOGGER.error("[SyncedConfig] Couldn't load config!", e);
        }
    }

    public void trySerialize(File file) {
        if (this.dirty) {
            try {
                this.serialize(file);
                this.dirty = false;
            } catch (ConfigException e) {
                ModKrowd.LOGGER.error("[SyncedConfig] Couldn't save config!", e);
            }
        }
    }

    public void copyFromConfig(Config source) {
        this.mergeState(source);

        this.selectedTab = source.selectedTab;

        this.dirty = true;
        this.updateFeatures();
    }

    public void updateTab(Config source) {
        this.selectedTab = source.selectedTab;
        this.dirty = true;
    }

    @Deprecated(forRemoval = true, since = "0.1.3")
    public void updateFeatures(MinecraftClient client, Subserver subserver) {
        this.updateFeatures(client, subserver, client.player != null ? ((EntityAccessor) client.player).callGetPermissionLevel() : 0);
    }

    public void updateFeatures() {
        MinecraftClient client = MinecraftClient.getInstance();
        this.updateFeatures(client, ModKrowd.currentSubserver, client.player != null ? ((EntityAccessor) client.player).callGetPermissionLevel() : 0);
    }

    public synchronized void updateFeatures(MinecraftClient client, Subserver subserver, int permissionLevel) {
        boolean disable = subserver != Subservers.PENDING;

        List<Feature> eventEnable = new ArrayList<>();
        List<Feature> eventDisable = new ArrayList<>();
        for (Feature f : this.features) {
            if (f.predicate.enabled(subserver, permissionLevel)) {
                if (!f.enabled) {
                    f.enabled = true;
                    this.enabledFeatures.add(f);
                    eventEnable.add(f);
                }
            } else {
                if (f.enabled && disable) {
                    f.enabled = false;
                    this.enabledFeatures.remove(f);
                    eventDisable.add(f);
                }
            }
        }
        ActionQueue queue = new ActionQueue();
        for (Feature f : eventEnable) {
            f.onEnable(client, queue);
        }
        for (Feature f : eventDisable) {
            f.onDisable(client, queue);
        }
        queue.flush(client);
    }

    public synchronized void onInitEnable(MinecraftClient client) {
        this.enabledFeatures.clear();
        ActionQueue queue = new ActionQueue();
        for (Feature f : this.features) {
            if (f.predicate.enabled(Subservers.NONE, 0)) {
                f.enabled = true;
                this.enabledFeatures.add(f);
                f.onInitEnable(client, queue);
            }
        }
        queue.flush(client);
    }

    public synchronized void onEndTick(MinecraftClient client) {
        ActionQueue queue = new ActionQueue();
        for (Feature f : this.enabledFeatures) {
            f.onEndTick(client, queue);
        }
        queue.flush(client);
    }

    public synchronized void onConfigurationComplete(ClientConfigurationNetworkHandler handler, MinecraftClient client) {
        ActionQueue queue = new ActionQueue();
        for (Feature f : this.enabledFeatures) {
            f.onConfigurationComplete(handler, client, queue);
        }
        queue.flush(client);
    }

    public synchronized void onJoin(ClientPlayNetworkHandler handler, MinecraftClient client) {
        ActionQueue queue = new ActionQueue();
        for (Feature f : this.enabledFeatures) {
            f.onJoin(handler, client, queue);
        }
        queue.flush(client);
    }

    public synchronized void onJoinUpdated(ClientPlayNetworkHandler handler, MinecraftClient client) {
        ActionQueue queue = new ActionQueue();
        for (Feature f : this.enabledFeatures) {
            f.onJoinUpdated(handler, client, queue);
        }
        queue.flush(client);
    }

    public synchronized void onDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client) {
        ActionQueue queue = new ActionQueue();
        for (Feature f : this.enabledFeatures) {
            f.onDisconnect(handler, client, queue);
        }
        queue.flush(client);
    }

    public synchronized void onMessage(MessageCache message, MinecraftClient client) {
        ActionQueue queue = new ActionQueue();
        for (Feature f : this.enabledFeatures) {
            f.onMessage(message, client, queue);
        }
        queue.flush(client);
    }

    public synchronized void onTabList(TabListCache tabList) {
        ActionQueue queue = new ActionQueue();
        MinecraftClient client = MinecraftClient.getInstance();
        for (Feature f : this.enabledFeatures) {
            f.onTabList(tabList, client, queue);
        }
        queue.flush(client);
    }

    public synchronized TextCache themeCustom(Custom custom) {
        ActionQueue queue = new ActionQueue();
        MinecraftClient client = MinecraftClient.getInstance();
        for (Feature f : this.enabledFeatures) {
            TextCache result = f.themeCustom(custom, client, queue);
            if (result != null) {
                queue.flush(client);
                return result;
            }
        }
        queue.flush(client);
        return custom.appearance();
    }
}
