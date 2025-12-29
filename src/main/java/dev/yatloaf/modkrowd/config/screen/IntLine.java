package dev.yatloaf.modkrowd.config.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class IntLine extends AbstractLine {
    private final int minValue;
    private final int maxValue;
    private final IntSupplier getter;
    private final IntConsumer setter;
    private final EditBox editBox;
    private int value;

    public IntLine(Component label, Tooltip tooltip, int startValue, int minValue, int maxValue, IntSupplier getter, IntConsumer setter) {
        super(false, label, tooltip);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.getter = getter;
        this.setter = setter;
        // X and Y are set by the parent layout
        this.editBox = new EditBox(Minecraft.getInstance().font, FeatureEntry.INPUT_WIDTH, FeatureEntry.LINE_HEIGHT, Component.empty());
        this.setValue(startValue);
        this.editBox.setFilter(s -> {
            if (s.isEmpty()) return true;
            try {
                int v = Integer.parseInt(s);
                return this.minValue <= v && v <= this.maxValue;
            } catch (NumberFormatException e) {
                return false;
            }
        });
        this.editBox.setResponder(s -> {
            this.value = s.isEmpty() ? 0 : Integer.parseInt(s);
            this.setter.accept(this.value);
        });
        this.horizontal.addChild(this.editBox);
        this.finish();
    }

    protected void setValue(int value) {
        this.value = value;
        this.editBox.setValue(Integer.toString(value));
    }

    @Override
    public void refreshState() {
        this.setValue(this.getter.getAsInt());
    }
}
