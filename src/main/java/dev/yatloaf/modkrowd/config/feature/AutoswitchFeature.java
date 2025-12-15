package dev.yatloaf.modkrowd.config.feature;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.config.PredicateIndex;
import dev.yatloaf.modkrowd.config.exception.MalformedConfigException;
import dev.yatloaf.modkrowd.config.screen.AbstractEntry;
import dev.yatloaf.modkrowd.config.screen.IntEntry;
import dev.yatloaf.modkrowd.config.screen.PredicateEntry;
import dev.yatloaf.modkrowd.cubekrowd.message.MissileWarsGameEndMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.MessageCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;

public class AutoswitchFeature extends Feature {
    public static final int MIN_DELAY = 0;
    public static final int MAX_DELAY = 280; // 14 seconds

    public final Component delayName;
    public final Tooltip delayTooltip;

    public int delay = 0;

    public AutoswitchFeature(String id, PredicateIndex allowedPredicates) {
        super(id, allowedPredicates);
        this.delayName = Component.translatable("modkrowd.config.feature." + id + ".delay");
        this.delayTooltip = Tooltip.create(Component.translatable("modkrowd.config.feature." + id + ".delay.tooltip"));
    }

    @Override
    public void mergeState(Feature source) {
        super.mergeState(source);
        if (source instanceof AutoswitchFeature cast) {
            this.delay = cast.delay;
        }
    }

    @Override
    public AbstractEntry[] createScreenEntries(Minecraft minecraft) {
        return new AbstractEntry[] {
                new PredicateEntry(minecraft, this),
                new IntEntry(
                        minecraft,
                        this.delayName,
                        this.delayTooltip,
                        this.delay,
                        MIN_DELAY,
                        MAX_DELAY,
                        () -> this.delay,
                        value -> this.delay = value
                )
        };
    }

    @Override
    public JsonElement serialize() {
        JsonObject result = new JsonObject();
        result.add("predicate", super.serialize());
        result.add("delay", new JsonPrimitive(this.delay));
        return result;
    }

    @Override
    public void deserialize(JsonElement source) throws MalformedConfigException {
        if (source.isJsonPrimitive()) {
            super.deserialize(source);
        } else {
            try {
                JsonObject object = GsonHelper.convertToJsonObject(source, this.id);
                super.deserialize(GsonHelper.getNonNull(object, "predicate"));
                int value = GsonHelper.getAsInt(object, "delay", 0);
                this.delay = Math.max(value, 0);
            } catch (JsonSyntaxException e) {
                throw new MalformedConfigException(e);
            }
        }
    }

    @Override
    public void onMessage(MessageCache message, Minecraft minecraft, ActionQueue queue) {
        if (message.result() instanceof MissileWarsGameEndMessage) {
            ModKrowd.startSwitchingMissileWarsLobby(this.delay);
        }
    }
}
