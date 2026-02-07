package dev.yatloaf.modkrowd.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.platform.InputConstants;
import dev.yatloaf.modkrowd.config.exception.MalformedConfigException;
import dev.yatloaf.modkrowd.config.feature.Feature;
import dev.yatloaf.modkrowd.config.screen.FeatureEntry;
import net.minecraft.util.GsonHelper;

public class FeatureState {
    public static final String ENABLED = "enabled";
    public static final String TOGGLE_KEY = "toggle_key";

    public static final String PREDICATE = "predicate";

    public final Feature feature;

    public boolean enabled = false;
    public InputConstants.Key toggleKey = InputConstants.UNKNOWN;

    public FeatureState(Feature feature) {
        this.feature = feature;
    }

    public void addOptions(FeatureEntry featureEntry) {

    }

    public void serialize(JsonObject dest) {
        dest.add(ENABLED, new JsonPrimitive(this.enabled));
        dest.add(TOGGLE_KEY, new JsonPrimitive(this.toggleKey.getName()));
    }

    public void deserialize(JsonObject source) throws MalformedConfigException {
        try {
            if (GsonHelper.isStringValue(source, PREDICATE)) {
                // TODO: Remove pre-0.3.0 handling
                this.enabled = !"never".equals(GsonHelper.getAsString(source, PREDICATE));
            } else {
                this.enabled = GsonHelper.getAsBoolean(source, ENABLED);
                this.toggleKey = InputConstants.getKey(GsonHelper.getAsString(source, TOGGLE_KEY));
            }
        } catch (JsonSyntaxException e) {
            throw new MalformedConfigException(e);
        }
    }

    public void imitate(FeatureState source) {
        this.enabled = source.enabled;
        this.toggleKey = source.toggleKey;
    }
}
