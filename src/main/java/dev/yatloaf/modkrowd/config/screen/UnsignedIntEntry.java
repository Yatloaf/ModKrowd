package dev.yatloaf.modkrowd.config.screen;

import dev.yatloaf.modkrowd.util.Util;
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

public class UnsignedIntEntry extends AbstractEntry {
    public final int maxValue;
    public final IntSupplier getter;
    public final IntConsumer setter;

    public final TextFieldWidget textField;

    private int value;

    public UnsignedIntEntry(MinecraftClient client, Text label, Tooltip tooltip, int startValue, IntSupplier getter, IntConsumer setter) {
        this(client, label, tooltip, startValue, Integer.MAX_VALUE, getter, setter);
    }

    public UnsignedIntEntry(MinecraftClient client, Text label, Tooltip tooltip, int startValue, int maxValue, IntSupplier getter, IntConsumer setter) {
        super(client, label);
        this.label.setTooltip(tooltip);
        this.maxValue = maxValue;
        this.getter = getter;
        this.setter = setter;
        this.textField = new TextFieldWidget(client.textRenderer, 0, 0, 64, 20, Text.empty()); // X and Y are set in render(...)
        this.setValue(startValue);
        this.textField.setTextPredicate(s -> {
                if (s.isEmpty()) return true;
                int v = Util.parseIntOr(s, -1);
                return 0 <= v && v <= this.maxValue;
        });
        this.textField.setChangedListener(s -> {
            this.value = s.isEmpty() ? 0 : Integer.parseUnsignedInt(s);
            this.setter.accept(this.value);
        });
    }

    protected void setValue(int value) {
        this.value = value;
        this.textField.setText(Integer.toString(value));
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        super.render(context, index, y, x, entryWidth, entryHeight, mouseX, mouseY, hovered, tickDelta);
        this.textField.setPosition(x + entryWidth - 64, y);
        this.textField.render(context, mouseX, mouseY, tickDelta);
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
