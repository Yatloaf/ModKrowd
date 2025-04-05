package dev.yatloaf.modkrowd.config.feature;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.config.PredicateIndex;
import dev.yatloaf.modkrowd.config.exception.MalformedConfigException;
import dev.yatloaf.modkrowd.config.screen.AbstractEntry;
import dev.yatloaf.modkrowd.config.screen.DoubleEntry;
import dev.yatloaf.modkrowd.config.screen.IdentifierEntry;
import dev.yatloaf.modkrowd.config.screen.PredicateEntry;
import dev.yatloaf.modkrowd.cubekrowd.message.DirectMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.Direction;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.CubeKrowdMessageCache;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.MessageCache;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class DirectMessageSoundFeature extends Feature {
    public static final Identifier DEFAULT_SOUND = SoundEvents.ENTITY_ARROW_HIT_PLAYER.getId();
    public static final double DEFAULT_VOLUME = 0.5;
    public static final double MIN_VOLUME = 0;
    public static final double MAX_VOLUME = 1;

    public final Text soundName;
    public final Tooltip soundTooltip;
    public final Text volumeName;
    public final Tooltip volumeTooltip;

    public Identifier sound = DEFAULT_SOUND;
    public double volume = DEFAULT_VOLUME;

    public DirectMessageSoundFeature(String id, PredicateIndex allowedPredicates) {
        super(id, allowedPredicates);
        this.soundName = Text.translatable("modkrowd.config.feature." + id + ".sound");
        this.soundTooltip = Tooltip.of(Text.translatable("modkrowd.config.feature." + id + ".sound.tooltip"));
        this.volumeName = Text.translatable("modkrowd.config.feature." + id + ".volume");
        this.volumeTooltip = Tooltip.of(Text.translatable("modkrowd.config.feature." + id + ".volume.tooltip"));
    }

    @Override
    public void mergeState(Feature source) {
        super.mergeState(source);
        if (source instanceof DirectMessageSoundFeature cast) {
            this.sound = cast.sound;
            this.volume = cast.volume;
        }
    }

    @Override
    public AbstractEntry[] createScreenEntries(MinecraftClient client) {
        return new AbstractEntry[] {
                new PredicateEntry(client, this),
                new IdentifierEntry(
                        client,
                        this.soundName,
                        this.soundTooltip,
                        this.sound,
                        () -> this.sound,
                        value -> this.sound = value.getPath().isBlank() ? DEFAULT_SOUND : value
                ),
                new DoubleEntry(
                        client,
                        this.volumeName,
                        this.volumeTooltip,
                        this.volume,
                        MIN_VOLUME,
                        MAX_VOLUME,
                        () -> this.volume,
                        value -> this.volume = value
                )
        };
    }

    @Override
    public JsonElement serialize() {
        JsonObject result = new JsonObject();
        result.add("predicate", super.serialize());
        result.add("sound", new JsonPrimitive(this.sound.toString()));
        result.add("volume", new JsonPrimitive(this.volume));
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
                String sound = JsonHelper.getString(object, "sound", "");
                this.sound = sound.isBlank() ? DEFAULT_SOUND : Identifier.of(sound);
                double volume = JsonHelper.getDouble(object, "volume", DEFAULT_VOLUME);
                this.volume = Math.clamp(volume, MIN_VOLUME, MAX_VOLUME);
            } catch (JsonSyntaxException e) {
                throw new MalformedConfigException(e);
            }
        }
    }

    @Override
    public void onMessage(MessageCache message, MinecraftClient client, ActionQueue queue) {
        if (message instanceof CubeKrowdMessageCache ckMessage) {
            DirectMessage dm = ckMessage.directMessageFast();
            if (dm.isReal() && dm.direction() != Direction.OUTGOING) {
                client.getSoundManager().play(PositionedSoundInstance.master(SoundEvent.of(this.sound), 1.0F, (float) this.volume));
            }
        }
    }
}
