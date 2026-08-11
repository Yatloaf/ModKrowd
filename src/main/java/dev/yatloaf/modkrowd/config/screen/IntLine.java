package dev.yatloaf.modkrowd.config.screen;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class IntLine extends ArgumentTypeLine<Integer> {
    public IntLine(Component label, Tooltip tooltip, Integer oldValue, int min, int max, Supplier<Integer> getter, Consumer<Integer> setter) {
        super(label, tooltip, oldValue, IntegerArgumentType.integer(min, max), getter, setter);
    }

    @Override
    protected String displayValue(Integer value) {
        return Integer.toString(value);
    }
}
