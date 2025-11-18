package dev.yatloaf.modkrowd.config.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * {@code Identifier} <3
 */
public class IdentifierEntry extends AbstractEntry {
    public final Supplier<ResourceLocation> getter;
    public final Consumer<ResourceLocation> setter;

    public final EditBox editBox;

    private ResourceLocation value;

    public IdentifierEntry(Minecraft minecraft, Component label, Tooltip tooltip, ResourceLocation startValue, Supplier<ResourceLocation> getter, Consumer<ResourceLocation> setter) {
        super(minecraft, label);
        this.label.setTooltip(tooltip);
        this.getter = getter;
        this.setter = setter;
        this.editBox = new EditBox(minecraft.font, 0, 0, 128, 20, Component.empty()); // X and Y are set in render(...)
        this.editBox.setMaxLength(256);
        this.setValue(startValue);
        this.editBox.setResponder(s -> {
            this.value = ResourceLocation.parse(s);
            this.setter.accept(this.value);
        });
    }

    protected void setValue(ResourceLocation value) {
        this.value = value;
        this.editBox.setValue(value.toString());
    }

    @Override
    public void renderContent(GuiGraphics context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        super.renderContent(context, mouseX, mouseY, hovered, deltaTicks);
        this.editBox.setPosition(this.getContentX() + this.getContentWidth() - 128, this.getContentY());
        this.editBox.render(context, mouseX, mouseY, deltaTicks);
    }

    @Override
    public void refreshState() {
        this.setValue(this.getter.get());
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
