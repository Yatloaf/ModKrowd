package dev.yatloaf.modkrowd.config.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

public class DoubleLine extends AbstractLine {
    private final double minValue;
    private final double maxValue;
    private final DoubleSupplier getter;
    private final DoubleConsumer setter;
    private final EditBox editBox;
    private double value;

    public DoubleLine(Component label, Tooltip tooltip, double startValue, double minValue, double maxValue, DoubleSupplier getter, DoubleConsumer setter) {
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
        this.horizontal.addChild(this.editBox);
        this.finish();
    }

    protected void setValue(double value) {
        this.value = value;
        this.editBox.setValue(Double.toString(value));
    }

    @Override
    public void refreshState() {
        this.setValue(this.getter.getAsDouble());
    }
}
