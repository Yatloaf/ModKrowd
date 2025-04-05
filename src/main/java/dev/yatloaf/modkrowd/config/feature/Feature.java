package dev.yatloaf.modkrowd.config.feature;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import dev.yatloaf.modkrowd.config.Predicate;
import dev.yatloaf.modkrowd.config.PredicateIndex;
import dev.yatloaf.modkrowd.config.exception.MalformedConfigException;
import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.config.screen.AbstractEntry;
import dev.yatloaf.modkrowd.config.screen.PredicateEntry;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.MessageCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import dev.yatloaf.modkrowd.custom.Custom;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientConfigurationNetworkHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.JsonHelper;

public class Feature {
    public final String id;
    public final MutableText name;
    public final MutableText tooltip;
    public final PredicateIndex allowedPredicates;

    // Only used in SyncedConfig
    public Predicate predicate = Predicate.NEVER;
    public boolean enabled = false;

    public Feature(String id, PredicateIndex allowedPredicates) {
        this.id = id;
        this.name = Text.translatable("modkrowd.config.feature." + id);
        this.tooltip = Text.translatable("modkrowd.config.feature." + id + ".tooltip");
        this.allowedPredicates = allowedPredicates;
    }

    public void mergeState(Feature source) {
        this.predicate = source.predicate;
        this.enabled = source.enabled;
    }

    public AbstractEntry[] createScreenEntries(MinecraftClient client) {
        return new AbstractEntry[]{
                new PredicateEntry(client, this)
        };
    }

    public JsonElement serialize() {
        return new JsonPrimitive(this.predicate.id);
    }

    public void deserialize(JsonElement source) throws MalformedConfigException {
        try {
            this.predicate = Predicate.FROM_ID.getOrDefault(JsonHelper.asString(source, this.id), Predicate.NEVER);
        } catch (JsonSyntaxException e) {
            throw new MalformedConfigException(e);
        }
    }

    /**
     * Called on startup if the feature is already enabled.
     *
     * @see ClientLifecycleEvents#CLIENT_STARTED
     *
     * @param client The {@link MinecraftClient} instance.
     * @param queue  The {@link ActionQueue} that is flushed after iterating through the features.
     */
    public void onInitEnable(MinecraftClient client, ActionQueue queue) {

    }

    /**
     * Called after the feature is enabled by entering a server or changing the settings.
     *
     * @param client The {@link MinecraftClient} instance.
     * @param queue  The {@link ActionQueue} that is flushed after iterating through the features.
     */
    public void onEnable(MinecraftClient client, ActionQueue queue) {

    }

    /**
     * Called after the feature is disabled by leaving a server or changing the settings.
     *
     * @param client The {@link MinecraftClient} instance.
     * @param queue  The {@link ActionQueue} that is flushed after iterating through the features.
     */
    public void onDisable(MinecraftClient client, ActionQueue queue) {

    }

    /**
     * Called before joining a server, while the subserver is {@code NONE}, if the feature is enabled.
     *
     * @see ClientConfigurationConnectionEvents#COMPLETE
     *
     * @param handler The {@link ClientPlayNetworkHandler} instance.
     * @param client  The {@link MinecraftClient} instance.
     * @param queue   The {@link ActionQueue} that is flushed after iterating through the features.
     */
    public void onConfigurationComplete(ClientConfigurationNetworkHandler handler, MinecraftClient client, ActionQueue queue) {

    }

    /**
     * Called when joining a server, while the subserver is {@code PENDING}, if the feature is enabled.
     *
     * @see ClientPlayConnectionEvents#JOIN
     *
     * @param handler The {@link ClientPlayNetworkHandler} instance.
     * @param client  The {@link MinecraftClient} instance.
     * @param queue   The {@link ActionQueue} that is flushed after iterating through the features.
     */
    public void onJoin(ClientPlayNetworkHandler handler, MinecraftClient client, ActionQueue queue) {

    }

    /**
     * Called after joining a server, when the {@code /whereami} response is received, if the feature is enabled.
     *
     * @param handler The {@link ClientPlayNetworkHandler} instance.
     * @param client  The {@link MinecraftClient} instance.
     * @param queue   The {@link ActionQueue} that is flushed after iterating through the features.
     */
    public void onJoinUpdated(ClientPlayNetworkHandler handler, MinecraftClient client, ActionQueue queue) {

    }

    /**
     * Called after leaving a server, before the subserver is reset, if the feature was enabled.
     *
     * @see ClientPlayConnectionEvents#DISCONNECT
     *
     * @param handler The {@link ClientPlayNetworkHandler} instance.
     * @param client  The {@link MinecraftClient} instance.
     * @param queue   The {@link ActionQueue} that is flushed after iterating through the features.
     */
    public void onDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client, ActionQueue queue) {

    }

    /**
     * Called at the end of a client tick if the feature is enabled.
     *
     * @see ClientTickEvents#END_CLIENT_TICK
     *
     * @param client The {@link MinecraftClient} instance.
     * @param queue  The {@link ActionQueue} that is flushed after iterating through the features.
     */
    public void onEndTick(MinecraftClient client, ActionQueue queue) {

    }

    /**
     * Called when a game message is received if the feature is enabled.
     *
     * @param message The message with convenient methods for parsing and modifying.
     * @param client  The {@link MinecraftClient} instance.
     * @param queue   The {@link ActionQueue} that is flushed after iterating through the features.
     */
    public void onMessage(MessageCache message, MinecraftClient client, ActionQueue queue) {

    }

    /**
     * Called when the tab list is updated if the feature is enabled.
     *
     * @param tabList The tab list with convenient methods for parsing and modifying.
     * @param client  The {@link MinecraftClient} instance.
     * @param queue   The {@link ActionQueue} that is flushed after iterating through the features.
     */
    public void onTabList(TabListCache tabList, MinecraftClient client, ActionQueue queue) {

    }

    /**
     * Allows a feature to theme a custom message sent by the mod itself, overriding the default style.
     *
     * @param custom The message to be themed.
     * @param client The {@link MinecraftClient} instance.
     * @param queue  The {@link ActionQueue} that is flushed after iterating through the features.
     * @return The themed custom message, or {@code null} if this feature does not theme it.
     */
    public TextCache themeCustom(Custom custom, MinecraftClient client, ActionQueue queue) {
        return null;
    }
}
