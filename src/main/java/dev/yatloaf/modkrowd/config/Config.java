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
import java.util.Map;

public class Config extends FeatureTree {
    public FeatureTab selectedTab;

    public Config() {
        this.selectedTab = this.APPEARANCE;
    }

    protected Config(Config source) {
        this.mergeState(source);
        this.selectedTab = this.tabs.get(source.selectedTab.index);
    }

    public Config copyConfig() {
        return new Config(this);
    }

    public void deserialize(File file) throws ReadConfigException {
        this.initExtenders();
        try (JsonReader jsonReader = new JsonReader(new BufferedReader(new FileReader(file)))) {

            JsonObject root = JsonParser.parseReader(jsonReader).getAsJsonObject();

            SemanticVersion version = getSemanticVersion(root, "version");
            if (version.compareTo(ModKrowd.VERSION) > 0) {
                throw new UnsupportedVersionConfigException(
                        "Expected version %s or lower, found %s".formatted(ModKrowd.VERSION.getFriendlyString(), version.getFriendlyString())
                );
            }

            this.selectedTab = this.idToTab.getOrDefault(GsonHelper.getAsString(root, "selected_tab", ""), this.APPEARANCE);

            if (GsonHelper.isObjectNode(root, "features")) {
                JsonObject features = GsonHelper.getAsJsonObject(root, "features");
                for (Map.Entry<String, JsonElement> featureEntry : features.entrySet()) {
                    String featureKey = featureEntry.getKey();
                    if (this.idToFeature.containsKey(featureKey)) {
                        this.idToFeature.get(featureKey).deserialize(featureEntry.getValue());
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
                    for (Feature f : this.features) {
                        jsonWriter.name(f.id);
                        jsonWriter.jsonValue(f.serialize().toString());
                    }
                jsonWriter.endObject();
            jsonWriter.endObject();

        } catch (IOException e) {
            throw new WriteConfigException(e);
        }
    }

    public void reset() {
        for (Feature f : this.features) {
            f.predicate = Predicate.NEVER;
        }
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
