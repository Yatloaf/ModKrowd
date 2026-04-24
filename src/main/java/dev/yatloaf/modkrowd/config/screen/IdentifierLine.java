package dev.yatloaf.modkrowd.config.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * {@code Identifier} <3
 */
public class IdentifierLine extends AbstractLine {
    public final Supplier<Identifier> getter;
    public final Consumer<Identifier> setter;

    public final EditBox editBox;

    private Identifier value;

    public IdentifierLine(Component label, Tooltip tooltip, Identifier startValue, Supplier<Identifier> getter, Consumer<Identifier> setter) {
        super(false, label, tooltip);
        this.getter = getter;
        this.setter = setter;
        // X and Y are set by the parent layout
        this.editBox = new EditBox(Minecraft.getInstance().font, FeatureEntry.INPUT_WIDTH, FeatureEntry.LINE_HEIGHT, Component.empty());
        this.editBox.setMaxLength(256);
        this.setValue(startValue);
        this.editBox.setResponder(s -> {
            this.value = Identifier.parse(s);
            this.setter.accept(this.value);
        });
        this.horizontal.addChild(this.editBox);
        this.finish();
    }

    protected void setValue(Identifier value) {
        this.value = value;
        this.editBox.setValue(value.toString());
    }

    @Override
    public void refreshState() {
        this.setValue(this.getter.get());
    }
}
