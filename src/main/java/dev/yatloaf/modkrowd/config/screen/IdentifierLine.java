package dev.yatloaf.modkrowd.config.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * {@code Identifier} <3
 */
public class IdentifierLine extends AbstractLine {
    public final Supplier<ResourceLocation> getter;
    public final Consumer<ResourceLocation> setter;

    public final EditBox editBox;

    private ResourceLocation value;

    public IdentifierLine(Component label, Tooltip tooltip, ResourceLocation startValue, Supplier<ResourceLocation> getter, Consumer<ResourceLocation> setter) {
        super(false, label, tooltip);
        this.getter = getter;
        this.setter = setter;
        // X and Y are set by the parent layout
        this.editBox = new EditBox(Minecraft.getInstance().font, FeatureEntry.INPUT_WIDTH, FeatureEntry.LINE_HEIGHT, Component.empty());
        this.editBox.setMaxLength(256);
        this.setValue(startValue);
        this.editBox.setResponder(s -> {
            this.value = ResourceLocation.parse(s);
            this.setter.accept(this.value);
        });
        this.horizontal.addChild(this.editBox);
        this.finish();
    }

    protected void setValue(ResourceLocation value) {
        this.value = value;
        this.editBox.setValue(value.toString());
    }

    @Override
    public void refreshState() {
        this.setValue(this.getter.get());
    }
}
