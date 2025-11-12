package dev.yatloaf.modkrowd.config.screen;

import com.google.common.collect.ImmutableList;
import dev.yatloaf.modkrowd.config.Predicate;
import dev.yatloaf.modkrowd.config.feature.Feature;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.CyclingButtonWidget;

import java.util.List;

public class PredicateEntry extends AbstractEntry {
    public final Feature feature;
    public final CyclingButtonWidget<Predicate> button;

    public PredicateEntry(MinecraftClient client, Feature feature) {
        super(client, feature.name);
        this.label.setTooltip(Tooltip.of(feature.tooltip));
        this.feature = feature;
        this.button = CyclingButtonWidget.<Predicate>builder(p -> p.name)
                .values(feature.allowedPredicates.index)
                .initially(feature.predicate)
                .omitKeyText()
                .tooltip(value -> Tooltip.of(value.tooltip))
                .build(0, 0, 64, 20, feature.name, this::onButton); // X and Y are set in render(...)
    }

    private void onButton(CyclingButtonWidget<Predicate> button, Predicate value) {
        this.feature.predicate = value;
    }

    @Override
    public void refreshState() {
        this.button.setValue(this.feature.predicate);
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return ImmutableList.of(this.button);
    }

    @Override
    public List<? extends Element> children() {
        return ImmutableList.of(this.button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        super.render(context, mouseX, mouseY, hovered, deltaTicks);
        this.button.setPosition(this.getContentX() + this.getContentWidth() - 64, this.getContentY());
        this.button.render(context, mouseX, mouseY, deltaTicks);
    }
}
