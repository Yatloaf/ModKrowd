package dev.yatloaf.modkrowd.config.screen;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.function.BooleanSupplier;

public class BooleanLine extends AbstractLine {
    private final BooleanSupplier getter;
    private final BooleanConsumer setter;
    private final CycleButton<Boolean> editButton;

    public BooleanLine(Component label, Tooltip tooltip, boolean startValue, BooleanSupplier getter, BooleanConsumer setter) {
        super(false, label, tooltip);
        this.getter = getter;
        this.setter = setter;
        // X and Y are set by the parent layout
        this.editButton = CycleButton.builder(this::displayBool)
                .withValues(false, true)
                .withInitialValue(startValue)
                .displayOnlyValue()
                .create(0, 0, FeatureEntry.INPUT_WIDTH, FeatureEntry.LINE_HEIGHT, label, this::onEdit);
        this.horizontal.addChild(this.editButton);
        this.finish();
    }

    private Component displayBool(boolean value) {
        return value ? CommonComponents.GUI_YES : CommonComponents.GUI_NO;
    }

    private void onEdit(CycleButton<Boolean> button, boolean value) {
        this.setter.accept(value);
    }

    @Override
    public void refreshState() {
        this.editButton.setValue(this.getter.getAsBoolean());
    }
}
