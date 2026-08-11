package dev.yatloaf.modkrowd.config.screen;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.DataResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class ArgumentTypeLine<T> extends AbstractLine {
    protected final ArgumentType<T> argument;
    protected final T oldValue;
    protected final Supplier<T> getter;
    protected final Consumer<T> setter;
    protected final EditBox editBox;
    protected T value;

    public ArgumentTypeLine(Component label, Tooltip tooltip, T oldValue, ArgumentType<T> argument, Supplier<T> getter, Consumer<T> setter) {
        super(false, label, tooltip);
        this.argument = argument;
        this.getter = getter;
        this.setter = setter;
        this.oldValue = oldValue;
        // X and Y are set by the parent layout
        this.editBox = new EditBox(Minecraft.getInstance().font, FeatureEntry.INPUT_WIDTH, FeatureEntry.LINE_HEIGHT, Component.empty());
        this.setValue(oldValue);
        this.editBox.setResponder(input -> {
            DataResult<T> result = this.deserialize(input);
            if (result.isSuccess()) {
                this.value = result.getOrThrow();
                this.editBox.setTextColor(EditBox.DEFAULT_TEXT_COLOR);
                this.setter.accept(this.value);
            } else {
                this.editBox.setTextColor(CommonColors.RED);
                this.setter.accept(this.oldValue);
            }
        });
        this.horizontal.addChild(this.editBox);
        this.finish();
    }

    // See `GameRule#deserialize`
    private DataResult<T> deserialize(String input) {
        if (input.isEmpty()) {
            return DataResult.success(this.oldValue);
        }
        try {
            StringReader reader = new StringReader(input);
            T result = this.argument.parse(reader);
            return reader.canRead() ? DataResult.error(() -> "Failed to deserialize; trailing characters", result) : DataResult.success(result);
        } catch (CommandSyntaxException _) {
            return DataResult.error(() -> "Failed to deserialize");
        }
    }

    protected void setValue(T value) {
        this.value = value;
        this.editBox.setValue(this.displayValue(value));
    }

    @Override
    public void refreshState() {
        this.setValue(this.getter.get());
    }

    protected abstract String displayValue(T value);
}
