package dev.yatloaf.modkrowd.config.screen;

import dev.yatloaf.modkrowd.config.Predicate;
import dev.yatloaf.modkrowd.config.feature.Feature;
import net.minecraft.client.gui.components.CycleButton;

public class HeaderLine extends AbstractLine {
    private final Feature feature;
    private final CycleButton<Predicate> button;

    public HeaderLine(Feature feature) {
        super(true, feature.name, feature.tooltip);
        this.feature = feature;
        // X and Y are set by the parent layout
        this.button = CycleButton.<Predicate>builder(p -> p.name)
                .withValues(feature.allowedPredicates.index)
                .withInitialValue(feature.predicate)
                .displayOnlyValue()
                .withTooltip(value -> value.tooltip)
                .create(0, 0, FeatureEntry.INPUT_WIDTH, FeatureEntry.LINE_HEIGHT, feature.name, this::onButton);
        this.horizontal.addChild(this.button);
        this.finish();
    }

    private void onButton(CycleButton<Predicate> button, Predicate value) {
        this.feature.predicate = value;
    }

    @Override
    public void refreshState() {
        this.button.setValue(this.feature.predicate);
    }
}
