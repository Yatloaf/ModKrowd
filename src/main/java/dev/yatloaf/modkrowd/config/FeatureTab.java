package dev.yatloaf.modkrowd.config;

import dev.yatloaf.modkrowd.config.feature.Feature;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class FeatureTab {
    public final int index;
    public final String id;
    public final Component name;
    public final List<Feature> features;

    public FeatureTab(int index, String id) {
        this.index = index;
        this.id = id;
        this.name = Component.translatable("modkrowd.config.tab." + id);
        this.features = new ArrayList<>();
    }

    public final <F extends Feature> F feature(F feature) {
        this.features.add(feature);
        Features.FEATURES.add(feature);
        Features.ID_TO_FEATURE.put(feature.id, feature);
        return feature;
    }
}
