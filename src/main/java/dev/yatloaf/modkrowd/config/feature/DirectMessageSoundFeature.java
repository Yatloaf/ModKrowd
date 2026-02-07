package dev.yatloaf.modkrowd.config.feature;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.config.FeatureState;
import dev.yatloaf.modkrowd.config.Restriction;
import dev.yatloaf.modkrowd.config.exception.MalformedConfigException;
import dev.yatloaf.modkrowd.config.screen.FeatureEntry;
import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.message.DirectMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.Direction;
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
    public final Component soundName;
    public final Tooltip soundTooltip;
    public final Component volumeName;
    public final Tooltip volumeTooltip;

    public DirectMessageSoundFeature(String id, Restriction restriction) {
        super(id, restriction);
        this.soundName = Component.translatable("modkrowd.config.feature." + id + ".sound").withStyle(CKColor.GRAY.style);
        this.soundTooltip = Tooltip.create(Component.translatable("modkrowd.config.feature." + id + ".sound.tooltip"));
        this.volumeName = Component.translatable("modkrowd.config.feature." + id + ".volume").withStyle(CKColor.GRAY.style);
        this.volumeTooltip = Tooltip.create(Component.translatable("modkrowd.config.feature." + id + ".volume.tooltip"));
    }

    @Override
    public FeatureState makeState() {
        return this.new State();
    }

    @Override
    public void onMessage(MessageCache message, Minecraft minecraft, ActionQueue queue) {
        if (message.result() instanceof DirectMessage dm && dm.direction() != Direction.OUTGOING && ModKrowd.CONFIG.getState(this) instanceof State state) {
            minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvent.createVariableRangeEvent(state.sound), 1.0F, (float) state.volume));
        }
    }

    public class State extends FeatureState {
        public static final String SOUND = "sound";
        public static final ResourceLocation DEFAULT_SOUND = SoundEvents.ARROW_HIT_PLAYER.location();
        public static final double DEFAULT_VOLUME = 0.5;
        public static final String VOLUME = "volume";
        public static final double MIN_VOLUME = 0;
        public static final double MAX_VOLUME = 1;

        public ResourceLocation sound = DEFAULT_SOUND;
        public double volume = DEFAULT_VOLUME;

        public State() {
            super(DirectMessageSoundFeature.this);
        }

        private ResourceLocation getSound() {
            return this.sound;
        }

        private void setSound(ResourceLocation value) {
            if (value.getPath().isBlank()) {
                this.sound = DEFAULT_SOUND;
            } else {
                this.sound = value;
            }
        }

        private double getVolume() {
            return this.volume;
        }

        private void setVolume(double value) {
            this.volume = value;
        }

        @Override
        public void addOptions(FeatureEntry featureEntry) {
            featureEntry.addIdentifier(DirectMessageSoundFeature.this.soundName, DirectMessageSoundFeature.this.soundTooltip,
                    this.sound, this::getSound, this::setSound);
            featureEntry.addDouble(DirectMessageSoundFeature.this.volumeName, DirectMessageSoundFeature.this.volumeTooltip,
                    this.volume, MIN_VOLUME, MAX_VOLUME, this::getVolume, this::setVolume);
        }

        @Override
        public void serialize(JsonObject dest) {
            super.serialize(dest);
            dest.add(SOUND, new JsonPrimitive(this.sound.toString()));
            dest.add(VOLUME, new JsonPrimitive(this.volume));
        }

        @Override
        public void deserialize(JsonObject source) throws MalformedConfigException {
            super.deserialize(source);
            this.sound = ResourceLocation.parse(GsonHelper.getAsString(source, SOUND, DEFAULT_SOUND.toString()));
            this.volume = Math.clamp(GsonHelper.getAsDouble(source, VOLUME, DEFAULT_VOLUME), MIN_VOLUME, MAX_VOLUME);
        }

        @Override
        public void imitate(FeatureState source) {
            super.imitate(source);
            if (source instanceof State state) {
                this.sound = state.sound;
                this.volume = state.volume;
            }
        }
    }
}
