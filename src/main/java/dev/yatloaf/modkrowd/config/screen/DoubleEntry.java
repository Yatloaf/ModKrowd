package dev.yatloaf.modkrowd.config.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class DoubleEntry extends AbstractEntry {
    public final double minValue;
    public final double maxValue;
    public final DoubleSupplier getter;
    public final DoubleConsumer setter;

    public final TextFieldWidget textField;

    private double value;

    public DoubleEntry(MinecraftClient client, Text label, Tooltip tooltip, double startValue, double minValue, double maxValue, DoubleSupplier getter, DoubleConsumer setter) {
        super(client, label);
        this.label.setTooltip(tooltip);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.getter = getter;
        this.setter = setter;
        this.textField = new TextFieldWidget(client.textRenderer, 0, 0, 64, 20, Text.empty()); // X and Y are set in render(...)
        this.setValue(startValue);
        this.textField.setTextPredicate(s -> {
            if (s.isEmpty()) return true;
            try {
                double v = Double.parseDouble(s);
                return this.minValue <= v && v <= this.maxValue;
            } catch (NumberFormatException e) {
                return false;
            }
        });
        this.textField.setChangedListener(s -> {
            this.value = s.isEmpty() ? 0 : Double.parseDouble(s);
            this.setter.accept(this.value);
        });
    }

    protected void setValue(double value) {
        this.value = value;
        this.textField.setText(Double.toString(value));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        super.render(context, mouseX, mouseY, hovered, deltaTicks);
        this.textField.setPosition(this.getContentX() + this.getContentWidth() - 64, this.getContentY());
        this.textField.render(context, mouseX, mouseY, deltaTicks);
    }

    @Override
    public void refreshState() {
        this.setValue(this.getter.getAsDouble());
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return List.of(this.textField);
    }

    @Override
    public List<? extends Element> children() {
        return List.of(this.textField);
    }
}
