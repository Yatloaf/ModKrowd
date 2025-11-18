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
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class IntEntry extends AbstractEntry {
    public final int minValue;
    public final int maxValue;
    public final IntSupplier getter;
    public final IntConsumer setter;

    public final EditBox editBox;

    private int value;

    public IntEntry(Minecraft minecraft, Component label, Tooltip tooltip, int startValue, int minValue, int maxValue, IntSupplier getter, IntConsumer setter) {
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
    }

    protected void setValue(int value) {
        this.value = value;
        this.editBox.setValue(Integer.toString(value));
    }

    @Override
    public void renderContent(GuiGraphics context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        super.renderContent(context, mouseX, mouseY, hovered, deltaTicks);
        this.editBox.setPosition(this.getContentX() + this.getContentWidth() - 64, this.getContentY());
        this.editBox.render(context, mouseX, mouseY, deltaTicks);
    }

    @Override
    public void refreshState() {
        this.setValue(this.getter.getAsInt());
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
