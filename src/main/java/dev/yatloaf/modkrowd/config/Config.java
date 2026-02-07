package dev.yatloaf.modkrowd.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.config.exception.MalformedConfigException;
import dev.yatloaf.modkrowd.config.exception.ReadConfigException;
import dev.yatloaf.modkrowd.config.exception.UnsupportedVersionConfigException;
import dev.yatloaf.modkrowd.config.exception.WriteConfigException;
import dev.yatloaf.modkrowd.config.feature.Feature;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.VersionParsingException;
import net.minecraft.util.GsonHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private final Map<Feature, FeatureState> states = new HashMap<>();

    public FeatureTab selectedTab;

    public Config() {
        for (Feature feature : Features.FEATURES) {
            this.states.put(feature, feature.makeState());
        }
        this.selectedTab = Features.APPEARANCE;
    }

    public Config(Config source) {
        for (Feature feature : Features.FEATURES) {
            FeatureState featureState = feature.makeState();
            featureState.imitate(source.getState(feature));
            this.states.put(feature, featureState);
        }
        this.selectedTab = source.selectedTab;
    }

    public void deserialize(File file) throws ReadConfigException {
        try (JsonReader jsonReader = new JsonReader(new BufferedReader(new FileReader(file)))) {

            JsonObject root = JsonParser.parseReader(jsonReader).getAsJsonObject();

            SemanticVersion version = getSemanticVersion(root, "version");
            if (version.compareTo(ModKrowd.VERSION) > 0) {
                throw new UnsupportedVersionConfigException(
                        "Expected version %s or lower, found %s".formatted(ModKrowd.VERSION.getFriendlyString(), version.getFriendlyString())
                );
            }

            this.selectedTab = Features.ID_TO_TAB.getOrDefault(GsonHelper.getAsString(root, "selected_tab", ""), Features.APPEARANCE);

            if (GsonHelper.isObjectNode(root, "features")) {
                JsonObject features = GsonHelper.getAsJsonObject(root, "features");
                for (Map.Entry<String, JsonElement> featureEntry : features.entrySet()) {
                    String key = featureEntry.getKey();
                    JsonElement value = featureEntry.getValue();

                    Feature feature = Features.ID_TO_FEATURE.get(key);
                    if (feature != null) {
                        FeatureState state = this.states.get(feature);

                        if (GsonHelper.isStringValue(value)) {
                            // TODO: Remove pre-0.3.0 handling
                            state.enabled = !"never".equals(GsonHelper.convertToString(value, key));
                        } else {
                            state.deserialize(GsonHelper.convertToJsonObject(value, key));
                        }
                    }
                }
            }

        } catch (JsonParseException e) {
            throw new MalformedConfigException(e);
        } catch (FileNotFoundException e) {
            this.reset();
        } catch (IOException e) {
            throw new ReadConfigException(e);
        }
    }

    public void serialize(File file) throws WriteConfigException {
        try (JsonWriter jsonWriter = new JsonWriter(new BufferedWriter(new FileWriter(file)))) {
            jsonWriter.setIndent("    ");

            jsonWriter.beginObject();
                jsonWriter.name("version").value(ModKrowd.VERSION.getFriendlyString());
                jsonWriter.name("selected_tab").value(this.selectedTab.id);
                jsonWriter.name("features").beginObject();
                    for (Feature feature : Features.FEATURES) {
                        jsonWriter.name(feature.id);
                        JsonObject dest = new JsonObject();
                        this.getState(feature).serialize(dest);
                        jsonWriter.jsonValue(dest.toString());
                    }
                jsonWriter.endObject();
            jsonWriter.endObject();

        } catch (IOException e) {
            throw new WriteConfigException(e);
        }
    }

    public void reset() {
        for (FeatureState featureState : this.states()) {
            featureState.enabled = false;
        }
    }

    public FeatureState getState(Feature feature) {
        return this.states.get(feature);
    }

    public Collection<FeatureState> states() {
        return this.states.values();
    }

    private static SemanticVersion getSemanticVersion(JsonObject object, String element) throws JsonParseException {
        if (object.has(element)) {
            return asSemanticVersion(object.get(element), element);
        } else {
            throw new JsonParseException("Missing " + element + ", expected to find a SemanticVersion");
        }
    }

    private static SemanticVersion asSemanticVersion(JsonElement element, String name) throws JsonParseException {
        String string = GsonHelper.convertToString(element, name);
        try {
            return SemanticVersion.parse(string);
        } catch (VersionParsingException e) {
            throw new JsonParseException("Expected " + element + " to be a SemanticVersion, was " + GsonHelper.getType(element));
        }
    }
}
