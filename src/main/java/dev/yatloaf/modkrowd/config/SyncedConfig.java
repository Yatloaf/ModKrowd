package dev.yatloaf.modkrowd.config;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.config.exception.ConfigException;
import dev.yatloaf.modkrowd.config.feature.Feature;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.MessageCache;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabDecoCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import dev.yatloaf.modkrowd.custom.Custom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SyncedConfig extends Config {
    public final List<Feature> enabledFeatures = new ArrayList<>();

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

    public void imitate(Config source) {
        for (FeatureState state : this.states()) {
            state.imitate(source.getState(state.feature));
        }

        this.selectedTab = source.selectedTab;

        this.dirty = true;
        this.updateFeatures();
    }

    public void updateTab(Config source) {
        this.selectedTab = source.selectedTab;
        this.dirty = true;
    }

    public void updateFeatures() {
        Minecraft minecraft = Minecraft.getInstance();
        this.updateFeatures(minecraft, ModKrowd.currentSubserver, minecraft.player != null ? minecraft.player.getPermissionLevel() : 0);
    }

    public synchronized void updateFeatures(Minecraft minecraft, Subserver subserver, int permissionLevel) {
        boolean disable = subserver != Subservers.PENDING;

        List<Feature> eventEnable = new ArrayList<>();
        List<Feature> eventDisable = new ArrayList<>();
        for (Feature feature : Features.FEATURES) {
            // Theoretically this should use a local map for whether the feature is active, and this solution assumes
            // there are no two conflicting SyncedConfig instances

            FeatureState featureState = this.getState(feature);
            if (featureState.enabled && feature.restriction.test.test(subserver, permissionLevel)) {
                if (!feature.active) {
                    feature.active = true;
                    this.enabledFeatures.add(feature);
                    eventEnable.add(feature);
                }
            } else {
                if (feature.active && disable) {
                    feature.active = false;
                    this.enabledFeatures.remove(feature);
                    eventDisable.add(feature);
                }
            }
        }
        ActionQueue queue = new ActionQueue();
        for (Feature f : eventEnable) {
            f.onEnable(minecraft, queue);
        }
        for (Feature f : eventDisable) {
            f.onDisable(minecraft, queue);
        }
        queue.flush(minecraft);
    }

    public synchronized void onInitEnable(Minecraft minecraft) {
        this.enabledFeatures.clear();
        ActionQueue queue = new ActionQueue();
        for (Feature feature : Features.FEATURES) {
            FeatureState featureState = this.getState(feature);
            if (featureState.enabled && feature.restriction.test.test(Subservers.NONE, 0)) {
                feature.active = true;
                this.enabledFeatures.add(feature);
                feature.onInitEnable(minecraft, queue);
            }
        }
        queue.flush(minecraft);
    }

    public synchronized void onEndTick(Minecraft minecraft) {
        ActionQueue queue = new ActionQueue();
        for (Feature f : this.enabledFeatures) {
            f.onEndTick(minecraft, queue);
        }
        queue.flush(minecraft);
    }

    public synchronized void onConfigurationComplete(ClientConfigurationPacketListenerImpl listener, Minecraft minecraft) {
        ActionQueue queue = new ActionQueue();
        for (Feature f : this.enabledFeatures) {
            f.onConfigurationComplete(listener, minecraft, queue);
        }
        queue.flush(minecraft);
    }

    public synchronized void onJoin(ClientPacketListener listener, Minecraft minecraft) {
        ActionQueue queue = new ActionQueue();
        for (Feature f : this.enabledFeatures) {
            f.onJoin(listener, minecraft, queue);
        }
        queue.flush(minecraft);
    }

    public synchronized void onJoinUpdated(ClientPacketListener listener, Minecraft minecraft) {
        ActionQueue queue = new ActionQueue();
        for (Feature f : this.enabledFeatures) {
            f.onJoinUpdated(listener, minecraft, queue);
        }
        queue.flush(minecraft);
    }

    public synchronized void onDisconnect(ClientPacketListener listener, Minecraft minecraft) {
        ActionQueue queue = new ActionQueue();
        for (Feature f : this.enabledFeatures) {
            f.onDisconnect(listener, minecraft, queue);
        }
        queue.flush(minecraft);
    }

    public synchronized void onMessage(MessageCache message, Minecraft minecraft) {
        ActionQueue queue = new ActionQueue();
        for (Feature f : this.enabledFeatures) {
            f.onMessage(message, minecraft, queue);
        }
        queue.flush(minecraft);
    }

    public synchronized void onTabList(TabListCache tabList) {
        ActionQueue queue = new ActionQueue();
        Minecraft minecraft = Minecraft.getInstance();
        for (Feature f : this.enabledFeatures) {
            f.onTabList(tabList, minecraft, queue);
        }
        queue.flush(minecraft);
    }

    public synchronized void onTabDeco(TabDecoCache tabDeco) {
        ActionQueue queue = new ActionQueue();
        Minecraft minecraft = Minecraft.getInstance();
        for (Feature f : this.enabledFeatures) {
            f.onTabDeco(tabDeco, minecraft, queue);
        }
        queue.flush(minecraft);
    }

    public synchronized TextCache themeCustom(Custom custom) {
        ActionQueue queue = new ActionQueue();
        Minecraft minecraft = Minecraft.getInstance();
        for (Feature f : this.enabledFeatures) {
            TextCache result = f.themeCustom(custom, minecraft, queue);
            if (result != null) {
                queue.flush(minecraft);
                return result;
            }
        }
        queue.flush(minecraft);
        return custom.appearance();
    }
}
