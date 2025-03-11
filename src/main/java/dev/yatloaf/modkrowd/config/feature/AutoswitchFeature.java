package dev.yatloaf.modkrowd.config.feature;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.config.exception.MalformedConfigException;
import dev.yatloaf.modkrowd.config.PredicateIndex;
import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.config.screen.AbstractEntry;
import dev.yatloaf.modkrowd.config.screen.PredicateEntry;
import dev.yatloaf.modkrowd.config.screen.UnsignedIntEntry;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.MessageCache;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.MissileWarsMessageCache;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;
import net.minecraft.util.JsonHelper;

public class AutoswitchFeature extends Feature {
    public static final int MAX_DELAY = 280; // 14 seconds

    public final Text delayName;
    public final Tooltip delayTooltip;

    public int delay = 0;

    public AutoswitchFeature(String id, PredicateIndex allowedPredicates) {
        super(id, allowedPredicates);
        this.delayName = Text.translatable("modkrowd.config.feature." + id + ".delay");
        this.delayTooltip = Tooltip.of(Text.translatable("modkrowd.config.feature." + id + ".delay.tooltip"));
    }

    @Override
    public void mergeState(Feature source) {
        super.mergeState(source);
        if (source instanceof AutoswitchFeature cast) {
            this.delay = cast.delay;
        }
    }

    @Override
    public AbstractEntry[] createScreenEntries(MinecraftClient client) {
        return new AbstractEntry[] {
                new PredicateEntry(client, this),
                new UnsignedIntEntry(
                        client,
                        this.delayName,
                        this.delayTooltip,
                        this.delay,
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
                JsonObject object = JsonHelper.asObject(source, this.id);
                super.deserialize(JsonHelper.getElement(object, "predicate"));
                int value = JsonHelper.getInt(object, "delay", 0);
                this.delay = Math.max(value, 0);
            } catch (JsonSyntaxException e) {
                throw new MalformedConfigException(e);
            }
        }
    }

    @Override
    public void onMessage(MessageCache message, boolean overlay, MinecraftClient client, ActionQueue queue) {
        if (message instanceof MissileWarsMessageCache mwCache && mwCache.missileWarsGameEndMessageFast().isReal()) {
            ModKrowd.startSwitchingMissileWarsLobby(this.delay);
        }
    }
}
