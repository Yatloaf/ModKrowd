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

    public final Component awaitMessageName;
    public final Tooltip awaitMessageTooltip;

    private boolean awaitingMessage = false;

    public AutoswitchFeature(String id, Restriction restriction) {
        super(id, restriction);
        this.delayName = Component.translatable("modkrowd.config.feature." + id + ".delay").withStyle(CKColor.GRAY.style);
        this.delayTooltip = Tooltip.create(Component.translatable("modkrowd.config.feature." + id + ".delay.tooltip"));
        this.awaitMessageName = Component.translatable("modkrowd.config.feature." + id + ".await_message").withStyle(CKColor.GRAY.style);
        this.awaitMessageTooltip = Tooltip.create(Component.translatable("modkrowd.config.feature." + id + ".await_message.tooltip"));
    }

    @Override
    public FeatureState makeState() {
        return this.new State();
    }

    @Override
    public void onMessage(MessageCache message, Minecraft minecraft, ActionQueue queue) {
        State state = (State) ModKrowd.CONFIG.getState(this);
        if (message.result() instanceof MissileWarsGameEndMessage) {
            if (state.awaitMessage) {
                this.awaitingMessage = true;
            } else {
                ModKrowd.startSwitchingMissileWarsLobby(state.delay);
            }
        }
    }

    public void onSendMessage() {
        State state = (State) ModKrowd.CONFIG.getState(this);
        if (state.awaitMessage && this.awaitingMessage) {
            this.awaitingMessage = false;
            ModKrowd.startSwitchingMissileWarsLobby(state.delay);
        }
    }

    public class State extends FeatureState {
        public static final String DELAY = "delay";
        public static final int DEFAULT_DELAY = 0;
        public static final int MIN_DELAY = 0;
        public static final int MAX_DELAY = 280; // 14 seconds

        public static final String AWAIT_MESSAGE = "await_message";
        public static final boolean DEFAULT_AWAIT_MESSAGE = false;

        public int delay;
        public boolean awaitMessage;

        public State() {
            super(AutoswitchFeature.this);
        }

        private int getDelay() {
            return this.delay;
        }

        private void setDelay(int delay) {
            this.delay = delay;
        }

        private boolean getAwaitMessage() {
            return this.awaitMessage;
        }

        private void setAwaitMessage(boolean awaitMessage) {
            this.awaitMessage = awaitMessage;
        }

        @Override
        public void addOptions(FeatureEntry featureEntry) {
            featureEntry.addInt(AutoswitchFeature.this.delayName, AutoswitchFeature.this.delayTooltip,
                    this.delay, MIN_DELAY, MAX_DELAY, this::getDelay, this::setDelay);
            featureEntry.addBoolean(AutoswitchFeature.this.awaitMessageName, AutoswitchFeature.this.awaitMessageTooltip,
                    this.awaitMessage, this::getAwaitMessage, this::setAwaitMessage);
        }

        @Override
        public void serialize(JsonObject dest) {
            super.serialize(dest);
            dest.add(DELAY, new JsonPrimitive(this.delay));
            dest.add(AWAIT_MESSAGE, new JsonPrimitive(this.awaitMessage));
        }

        @Override
        public void deserialize(JsonObject source) throws MalformedConfigException {
            super.deserialize(source);
            this.delay = Math.max(GsonHelper.getAsInt(source, DELAY, DEFAULT_DELAY), MIN_DELAY);
            this.awaitMessage = GsonHelper.getAsBoolean(source, AWAIT_MESSAGE, DEFAULT_AWAIT_MESSAGE);
        }

        @Override
        public void imitate(FeatureState source) {
            super.imitate(source);
            if (source instanceof State state) {
                this.delay = state.delay;
                this.awaitMessage = state.awaitMessage;
            }
        }
    }
}
