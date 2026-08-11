package dev.yatloaf.modkrowd.config.screen;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class DoubleLine extends ArgumentTypeLine<Double> {
    public DoubleLine(Component label, Tooltip tooltip, Double oldValue, double min, double max, Supplier<Double> getter, Consumer<Double> setter) {
        super(label, tooltip, oldValue, DoubleArgumentType.doubleArg(min, max), getter, setter);
    }

    @Override
    protected String displayValue(Double value) {
        return Double.toString(value);
    }
}
