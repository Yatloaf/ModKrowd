package dev.yatloaf.modkrowd.config.feature;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.config.Restriction;
import dev.yatloaf.modkrowd.config.exception.MalformedConfigException;
import dev.yatloaf.modkrowd.config.screen.FeatureEntry;
import dev.yatloaf.modkrowd.config.FeatureState;
import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.message.MissileWarsGameEndMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.MessageCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;

public class AutoswitchFeature extends Feature {
    public final Component delayName;
    public final Tooltip delayTooltip;

    public AutoswitchFeature(String id, Restriction restriction) {
        super(id, restriction);
        this.delayName = Component.translatable("modkrowd.config.feature." + id + ".delay").withStyle(CKColor.GRAY.style);
        this.delayTooltip = Tooltip.create(Component.translatable("modkrowd.config.feature." + id + ".delay.tooltip"));
    }

    @Override
    public FeatureState makeState() {
        return this.new State();
    }

    @Override
    public void onMessage(MessageCache message, Minecraft minecraft, ActionQueue queue) {
        if (message.result() instanceof MissileWarsGameEndMessage && ModKrowd.CONFIG.getState(this) instanceof State state) {
            ModKrowd.startSwitchingMissileWarsLobby(state.delay);
        }
    }

    public class State extends FeatureState {
        public static final String DELAY = "delay";
        public static final int MIN_DELAY = 0;
        public static final int MAX_DELAY = 280; // 14 seconds

        public int delay;

        public State() {
            super(AutoswitchFeature.this);
        }

        private int getDelay() {
            return this.delay;
        }

        private void setDelay(int delay) {
            this.delay = delay;
        }

        @Override
        public void addOptions(FeatureEntry featureEntry) {
            featureEntry.addInt(AutoswitchFeature.this.delayName, AutoswitchFeature.this.delayTooltip,
                    this.delay, MIN_DELAY, MAX_DELAY, this::getDelay, this::setDelay);
        }

        @Override
        public void serialize(JsonObject dest) {
            super.serialize(dest);
            dest.add(DELAY, new JsonPrimitive(this.delay));
        }

        @Override
        public void deserialize(JsonObject source) throws MalformedConfigException {
            super.deserialize(source);
            this.delay = Math.max(GsonHelper.getAsInt(source, "delay", 0), 0);
        }

        @Override
        public void imitate(FeatureState source) {
            super.imitate(source);
            if (source instanceof State state) {
                this.delay = state.delay;
            }
        }
    }
}
