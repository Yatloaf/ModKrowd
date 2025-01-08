package dev.yatloaf.modkrowd.config.api;

import dev.yatloaf.modkrowd.config.FeatureTab;
import dev.yatloaf.modkrowd.config.FeatureTree;
import dev.yatloaf.modkrowd.config.feature.Feature;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

/**
 * Implementation of this interface allows other mods to add custom features and tabs to the ModKrowd configuration screen and file.
 */
@FunctionalInterface
public interface FeatureExtender {
    /**
     * Register a {@link FeatureExtender} callback which is called for every {@link FeatureTree} instance.
     * <p>
     * <b>This must be done before {@link ClientLifecycleEvents#CLIENT_STARTED},
     * preferably within {@code onInitialize} or {@code onInitializeClient}.</b>
     *
     * @param extender The {@link FeatureExtender} to be registered
     */
    static void register(FeatureExtender extender) {
        FeatureTree.registerExtender(extender);
    }

    /**
     * Extend the {@link FeatureTree}, probably using {@link FeatureTree#tab(String)} and {@link FeatureTab#feature(Feature)}.
     *
     * @param source The {@link FeatureTree} to be extended
     */
    void extend(FeatureTree source);
}
