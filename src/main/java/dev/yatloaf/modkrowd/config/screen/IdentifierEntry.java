package dev.yatloaf.modkrowd.config.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class IdentifierEntry extends AbstractEntry {
    public final Supplier<Identifier> getter;
    public final Consumer<Identifier> setter;

    public final TextFieldWidget textField;

    private Identifier value;

    public IdentifierEntry(MinecraftClient client, Text label, Tooltip tooltip, Identifier startValue, Supplier<Identifier> getter, Consumer<Identifier> setter) {
        super(client, label);
        this.label.setTooltip(tooltip);
        this.getter = getter;
        this.setter = setter;
        this.textField = new TextFieldWidget(client.textRenderer, 0, 0, 128, 20, Text.empty()); // X and Y are set in render(...)
        this.textField.setMaxLength(256);
        this.setValue(startValue);
        this.textField.setChangedListener(s -> {
            this.value = Identifier.of(s);
            this.setter.accept(this.value);
        });
    }

    protected void setValue(Identifier value) {
        this.value = value;
        this.textField.setText(value.toString());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        super.render(context, mouseX, mouseY, hovered, deltaTicks);
        this.textField.setPosition(this.getContentX() + this.getContentWidth() - 128, this.getContentY());
        this.textField.render(context, mouseX, mouseY, deltaTicks);
    }

    @Override
    public void refreshState() {
        this.setValue(this.getter.get());
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
