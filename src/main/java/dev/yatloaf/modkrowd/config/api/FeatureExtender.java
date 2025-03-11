package dev.yatloaf.modkrowd.config.api;

import dev.yatloaf.modkrowd.config.FeatureTab;
import dev.yatloaf.modkrowd.config.FeatureTree;
import dev.yatloaf.modkrowd.config.feature.Feature;

/**
 * Implementation of this entrypoint allows other mods to add custom features and tabs to the ModKrowd configuration screen and file.
 * The entrypoint should be listed in {@code fabric.mod.json} with the key {@code modkrowd:feature_extender}.
 */
@FunctionalInterface
public interface FeatureExtender {
    /**
     * Extend the {@link FeatureTree}, probably using {@link FeatureTree#tab(String)} and {@link FeatureTab#feature(Feature)}.
     *
     * @param source The {@link FeatureTree} to be extended
     */
    void extend(FeatureTree source);
}
