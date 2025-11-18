package dev.yatloaf.modkrowd.config.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class DoubleEntry extends AbstractEntry {
    public final double minValue;
    public final double maxValue;
    public final DoubleSupplier getter;
    public final DoubleConsumer setter;

    public final EditBox editBox;

    private double value;

    public DoubleEntry(Minecraft minecraft, Component label, Tooltip tooltip, double startValue, double minValue, double maxValue, DoubleSupplier getter, DoubleConsumer setter) {
        super(minecraft, label);
        this.label.setTooltip(tooltip);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.getter = getter;
        this.setter = setter;
        this.editBox = new EditBox(minecraft.font, 0, 0, 64, 20, Component.empty()); // X and Y are set in render(...)
        this.setValue(startValue);
        this.editBox.setFilter(s -> {
            if (s.isEmpty()) return true;
            try {
                double v = Double.parseDouble(s);
                return this.minValue <= v && v <= this.maxValue;
            } catch (NumberFormatException e) {
                return false;
            }
        });
        this.editBox.setResponder(s -> {
            this.value = s.isEmpty() ? 0 : Double.parseDouble(s);
            this.setter.accept(this.value);
        });
    }

    protected void setValue(double value) {
        this.value = value;
        this.editBox.setValue(Double.toString(value));
    }

    @Override
    public void renderContent(GuiGraphics context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        super.renderContent(context, mouseX, mouseY, hovered, deltaTicks);
        this.editBox.setPosition(this.getContentX() + this.getContentWidth() - 64, this.getContentY());
        this.editBox.render(context, mouseX, mouseY, deltaTicks);
    }

    @Override
    public void refreshState() {
        this.setValue(this.getter.getAsDouble());
    }

    @Override
    public @NotNull List<? extends NarratableEntry> narratables() {
        return List.of(this.editBox);
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return List.of(this.editBox);
    }
}
