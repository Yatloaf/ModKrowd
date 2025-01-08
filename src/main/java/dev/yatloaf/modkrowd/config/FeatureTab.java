package dev.yatloaf.modkrowd.config;

import dev.yatloaf.modkrowd.config.feature.Feature;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeatureTab {
    private final FeatureTree parent;

    public final int index;
    public final String id;
    public final Text name;
    public final List<Feature> features;
    public final Map<String, Feature> idToFeature;

    public FeatureTab(FeatureTree parent, int index, String id) {
        this.parent = parent;
        this.index = index;
        this.id = id;
        this.name = Text.translatable("modkrowd.config.tab." + id);
        this.features = new ArrayList<>();
        this.idToFeature = new HashMap<>();
    }

    public final <F extends Feature> F feature(F feature) {
        this.parent.features.add(feature);
        this.parent.idToFeature.put(feature.id, feature);
        this.features.add(feature);
        this.idToFeature.put(feature.id, feature);
        return feature;
    }
}
