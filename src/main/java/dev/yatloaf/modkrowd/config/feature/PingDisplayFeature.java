package dev.yatloaf.modkrowd.config.feature;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.config.FeatureState;
import dev.yatloaf.modkrowd.config.Restriction;
import dev.yatloaf.modkrowd.config.exception.MalformedConfigException;
import dev.yatloaf.modkrowd.config.screen.FeatureEntry;
import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.GsonHelper;

public class PingDisplayFeature extends Feature {
    public final Component abbreviateName;
    public final Tooltip abbreviateTooltip;
    public final Component narrowFontName;
    public final Tooltip narrowFontTooltip;

    public PingDisplayFeature(String id, Restriction restriction) {
        super(id, restriction);
        this.abbreviateName = Component.translatable("modkrowd.config.feature." + id + ".abbreviate").withStyle(CKColor.GRAY.style);
        this.abbreviateTooltip = Tooltip.create(Component.translatable("modkrowd.config.feature." + id + ".abbreviate.tooltip"));
        this.narrowFontName = Component.translatable("modkrowd.config.feature." + id + ".narrow_font").withStyle(CKColor.GRAY.style);
        this.narrowFontTooltip = Tooltip.create(Component.translatable("modkrowd.config.feature." + id + ".narrow_font.tooltip"));
    }

    @Override
    public FeatureState makeState() {
        return this.new State();
    }

    public class State extends FeatureState {
        public static final String ABBREVIATE = "abbreviate";
        public static final boolean DEFAULT_ABBREVIATE = false;
        public static final String NARROW_FONT = "narrow_font";
        public static final boolean DEFAULT_NARROW_FONT = false;

        public boolean abbreviate = DEFAULT_ABBREVIATE;
        public boolean narrowFont = DEFAULT_NARROW_FONT;

        public State() {
            super(PingDisplayFeature.this);
        }

        public Style style() {
            return this.narrowFont ? ModKrowd.NARROW_FONT_STYLE : Style.EMPTY;
        }

        private boolean getAbbreviate() {
            return this.abbreviate;
        }

        private void setAbbreviate(boolean value) {
            this.abbreviate = value;
        }

        private boolean getNarrowFont() {
            return this.narrowFont;
        }

        private void setNarrowFont(boolean value) {
            this.narrowFont = value;
        }

        @Override
        public void addOptions(FeatureEntry featureEntry) {
            featureEntry.addBoolean(PingDisplayFeature.this.abbreviateName, PingDisplayFeature.this.abbreviateTooltip,
                    this.abbreviate, this::getAbbreviate, this::setAbbreviate);
            featureEntry.addBoolean(PingDisplayFeature.this.narrowFontName, PingDisplayFeature.this.narrowFontTooltip,
                    this.narrowFont, this::getNarrowFont, this::setNarrowFont);
        }

        @Override
        public void serialize(JsonObject dest) {
            super.serialize(dest);
            dest.add(ABBREVIATE, new JsonPrimitive(this.abbreviate));
            dest.add(NARROW_FONT, new JsonPrimitive(this.narrowFont));
        }

        @Override
        public void deserialize(JsonObject source) throws MalformedConfigException {
            super.deserialize(source);
            this.abbreviate = GsonHelper.getAsBoolean(source, ABBREVIATE, DEFAULT_ABBREVIATE);
            this.narrowFont = GsonHelper.getAsBoolean(source, NARROW_FONT, DEFAULT_NARROW_FONT);
        }

        @Override
        public void imitate(FeatureState source) {
            super.imitate(source);
            if (source instanceof State state) {
                this.abbreviate = state.abbreviate;
                this.narrowFont = state.narrowFont;
            }
        }
    }
}
