package dev.yatloaf.modkrowd.config.screen;

import com.google.common.collect.ImmutableList;
import dev.yatloaf.modkrowd.config.Predicate;
import dev.yatloaf.modkrowd.config.feature.Feature;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PredicateEntry extends AbstractEntry {
    public final Feature feature;
    public final CycleButton<Predicate> button;

    public PredicateEntry(Minecraft minecraft, Feature feature) {
        super(minecraft, feature.name);
        this.label.setTooltip(Tooltip.create(feature.tooltip));
        this.feature = feature;
        this.button = CycleButton.<Predicate>builder(p -> p.name)
                .withValues(feature.allowedPredicates.index)
                .withInitialValue(feature.predicate)
                .displayOnlyValue()
                .withTooltip(value -> Tooltip.create(value.tooltip))
                .create(0, 0, 64, 20, feature.name, this::onButton); // X and Y are set in render(...)
    }

    private void onButton(CycleButton<Predicate> button, Predicate value) {
        this.feature.predicate = value;
    }

    @Override
    public void refreshState() {
        this.button.setValue(this.feature.predicate);
    }

    @Override
    public @NotNull List<? extends NarratableEntry> narratables() {
        return ImmutableList.of(this.button);
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return ImmutableList.of(this.button);
    }

    @Override
    public void renderContent(GuiGraphics context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        super.renderContent(context, mouseX, mouseY, hovered, deltaTicks);
        this.button.setPosition(this.getContentX() + this.getContentWidth() - 64, this.getContentY());
        this.button.render(context, mouseX, mouseY, deltaTicks);
    }
}
