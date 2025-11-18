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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.GsonHelper;

public class DirectMessageSoundFeature extends Feature {
    public static final ResourceLocation DEFAULT_SOUND = SoundEvents.ARROW_HIT_PLAYER.location();
    public static final double DEFAULT_VOLUME = 0.5;
    public static final double MIN_VOLUME = 0;
    public static final double MAX_VOLUME = 1;

    public final Component soundName;
    public final Tooltip soundTooltip;
    public final Component volumeName;
    public final Tooltip volumeTooltip;

    public ResourceLocation sound = DEFAULT_SOUND;
    public double volume = DEFAULT_VOLUME;

    public DirectMessageSoundFeature(String id, PredicateIndex allowedPredicates) {
        super(id, allowedPredicates);
        this.soundName = Component.translatable("modkrowd.config.feature." + id + ".sound");
        this.soundTooltip = Tooltip.create(Component.translatable("modkrowd.config.feature." + id + ".sound.tooltip"));
        this.volumeName = Component.translatable("modkrowd.config.feature." + id + ".volume");
        this.volumeTooltip = Tooltip.create(Component.translatable("modkrowd.config.feature." + id + ".volume.tooltip"));
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
    public AbstractEntry[] createScreenEntries(Minecraft minecraft) {
        return new AbstractEntry[] {
                new PredicateEntry(minecraft, this),
                new IdentifierEntry(
                        minecraft,
                        this.soundName,
                        this.soundTooltip,
                        this.sound,
                        () -> this.sound,
                        value -> this.sound = value.getPath().isBlank() ? DEFAULT_SOUND : value
                ),
                new DoubleEntry(
                        minecraft,
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
                JsonObject object = GsonHelper.convertToJsonObject(source, this.id);
                super.deserialize(GsonHelper.getNonNull(object, "predicate"));
                String sound = GsonHelper.getAsString(object, "sound", "");
                this.sound = sound.isBlank() ? DEFAULT_SOUND : ResourceLocation.parse(sound);
                double volume = GsonHelper.getAsDouble(object, "volume", DEFAULT_VOLUME);
                this.volume = Math.clamp(volume, MIN_VOLUME, MAX_VOLUME);
            } catch (JsonSyntaxException e) {
                throw new MalformedConfigException(e);
            }
        }
    }

    @Override
    public void onMessage(MessageCache message, Minecraft minecraft, ActionQueue queue) {
        if (message instanceof CubeKrowdMessageCache ckMessage) {
            DirectMessage dm = ckMessage.directMessageFast();
            if (dm.isReal() && dm.direction() != Direction.OUTGOING) {
                minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvent.createVariableRangeEvent(this.sound), 1.0F, (float) this.volume));
            }
        }
    }
}
