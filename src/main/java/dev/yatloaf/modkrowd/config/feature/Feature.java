package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.config.FeatureState;
import dev.yatloaf.modkrowd.config.Restriction;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.MessageCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabDecoCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import dev.yatloaf.modkrowd.custom.Custom;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.multiplayer.ClientConfigurationPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class Feature {
    public final String id;
    public final MutableComponent name;
    public final Tooltip tooltip;
    public final Restriction restriction;

    /// Should really be in a map inside `SyncedConfig`, but this is more convenient
    public boolean active = false;

    public Feature(String id, Restriction restriction) {
        this.id = id;
        this.name = Component.translatable("modkrowd.config.feature." + id);
        MutableComponent tooltip = Component.translatable("modkrowd.config.feature." + id + ".tooltip");
        if (restriction.suffix != null) {
            tooltip.append("\n").append(restriction.suffix);
        }
        this.tooltip = Tooltip.create(tooltip);
        this.restriction = restriction;
    }

    public FeatureState makeState() {
        return new FeatureState(this);
    }

    /**
     * Called on startup if the feature is already enabled.
     *
     * @see ClientLifecycleEvents#CLIENT_STARTED
     *
     * @param minecraft The {@link Minecraft} instance.
     * @param queue     The {@link ActionQueue} that is flushed after iterating through the features.
     */
    public void onInitEnable(Minecraft minecraft, ActionQueue queue) {

    }

    /**
     * Called after the feature is enabled by entering a server or changing the settings.
     *
     * @param minecraft The {@link Minecraft} instance.
     * @param queue     The {@link ActionQueue} that is flushed after iterating through the features.
     */
    public void onEnable(Minecraft minecraft, ActionQueue queue) {

    }

    /**
     * Called after the feature is disabled by leaving a server or changing the settings.
     *
     * @param minecraft The {@link Minecraft} instance.
     * @param queue     The {@link ActionQueue} that is flushed after iterating through the features.
     */
    public void onDisable(Minecraft minecraft, ActionQueue queue) {

    }

    /**
     * Called before joining a server, while the subserver is {@code NONE}, if the feature is enabled.
     *
     * @see ClientConfigurationConnectionEvents#COMPLETE
     *
     * @param listener  The {@link ClientPacketListener} instance.
     * @param minecraft The {@link Minecraft} instance.
     * @param queue     The {@link ActionQueue} that is flushed after iterating through the features.
     */
    public void onConfigurationComplete(ClientConfigurationPacketListenerImpl listener, Minecraft minecraft, ActionQueue queue) {

    }

    /**
     * Called when joining a server, while the subserver is {@code PENDING}, if the feature is enabled.
     *
     * @see ClientPlayConnectionEvents#JOIN
     *
     * @param listener  The {@link ClientPacketListener} instance.
     * @param minecraft The {@link Minecraft} instance.
     * @param queue     The {@link ActionQueue} that is flushed after iterating through the features.
     */
    public void onJoin(ClientPacketListener listener, Minecraft minecraft, ActionQueue queue) {

    }

    /**
     * Called after joining a server, when the {@code /whereami} response is received, if the feature is enabled.
     *
     * @param listener  The {@link ClientPacketListener} instance.
     * @param minecraft The {@link Minecraft} instance.
     * @param queue     The {@link ActionQueue} that is flushed after iterating through the features.
     */
    public void onJoinUpdated(ClientPacketListener listener, Minecraft minecraft, ActionQueue queue) {

    }

    /**
     * Called after leaving a server, before the subserver is reset, if the feature was enabled.
     *
     * @see ClientPlayConnectionEvents#DISCONNECT
     *
     * @param listener  The {@link ClientPacketListener} instance.
     * @param minecraft The {@link Minecraft} instance.
     * @param queue     The {@link ActionQueue} that is flushed after iterating through the features.
     */
    public void onDisconnect(ClientPacketListener listener, Minecraft minecraft, ActionQueue queue) {

    }

    /**
     * Called at the end of a client tick if the feature is enabled.
     *
     * @see ClientTickEvents#END_CLIENT_TICK
     *
     * @param minecraft The {@link Minecraft} instance.
     * @param queue     The {@link ActionQueue} that is flushed after iterating through the features.
     */
    public void onEndTick(Minecraft minecraft, ActionQueue queue) {

    }

    /**
     * Called when a game message is received if the feature is enabled.
     *
     * @param message   The message with convenient methods for parsing and modifying.
     * @param minecraft The {@link Minecraft} instance.
     * @param queue     The {@link ActionQueue} that is flushed after iterating through the features.
     */
    public void onMessage(MessageCache message, Minecraft minecraft, ActionQueue queue) {

    }

    /**
     * Called when the tab list is updated if the feature is enabled.
     *
     * @param tabList The tab list with convenient methods for parsing and modifying.
     * @param minecraft  The {@link Minecraft} instance.
     * @param queue   The {@link ActionQueue} that is flushed after iterating through the features.
     */
    public void onTabList(TabListCache tabList, Minecraft minecraft, ActionQueue queue) {

    }

    /**
     * Called when the tab decoration (header or footer) is updated if the feature is enabled.
     *
     * @param tabDeco   The tab decoration with convenient methods for parsing and modifying.
     * @param minecraft The {@link Minecraft} instance.
     * @param queue     The {@link ActionQueue} that is flushed after iterating through the features.
     */
    public void onTabDeco(TabDecoCache tabDeco, Minecraft minecraft, ActionQueue queue) {

    }

    /**
     * Allows a feature to theme a custom message sent by the mod itself, overriding the default style.
     *
     * @param custom    The message to be themed.
     * @param minecraft The {@link Minecraft} instance.
     * @param queue     The {@link ActionQueue} that is flushed after iterating through the features.
     * @return The themed custom message, or {@code null} if this feature does not theme it.
     */
    public TextCache themeCustom(Custom custom, Minecraft minecraft, ActionQueue queue) {
        return null;
    }
}
