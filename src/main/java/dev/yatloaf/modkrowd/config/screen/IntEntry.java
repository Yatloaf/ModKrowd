package dev.yatloaf.modkrowd.config.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class IntEntry extends AbstractEntry {
    public final int minValue;
    public final int maxValue;
    public final IntSupplier getter;
    public final IntConsumer setter;

    public final TextFieldWidget textField;

    private int value;

    public IntEntry(MinecraftClient client, Text label, Tooltip tooltip, int startValue, int minValue, int maxValue, IntSupplier getter, IntConsumer setter) {
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
                int v = Integer.parseInt(s);
                return this.minValue <= v && v <= this.maxValue;
            } catch (NumberFormatException e) {
                return false;
            }
        });
        this.textField.setChangedListener(s -> {
            this.value = s.isEmpty() ? 0 : Integer.parseInt(s);
            this.setter.accept(this.value);
        });
    }

    protected void setValue(int value) {
        this.value = value;
        this.textField.setText(Integer.toString(value));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        super.render(context, mouseX, mouseY, hovered, deltaTicks);
        this.textField.setPosition(this.getContentX() + this.getContentWidth() - 64, this.getContentY());
        this.textField.render(context, mouseX, mouseY, deltaTicks);
    }

    @Override
    public void refreshState() {
        this.setValue(this.getter.getAsInt());
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
