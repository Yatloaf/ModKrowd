package dev.yatloaf.modkrowd.config.screen;

import com.mojang.blaze3d.platform.InputConstants;
import dev.yatloaf.modkrowd.config.FeatureState;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.function.Supplier;

public class HeaderLine extends AbstractLine {
    public static final Component ON = CommonComponents.OPTION_ON.copy().withStyle(ChatFormatting.GREEN);
    public static final Component OFF = CommonComponents.OPTION_OFF.copy().withStyle(ChatFormatting.RED);
    public static final Tooltip KEY_BIND_TOOLTIP = Tooltip.create(Component.translatable("modkrowd.config.bind_toggle_key.tooltip"));

    private final ConfigScreen screen;
    private final FeatureState state;
    private final CycleButton<Boolean> toggleButton;
    private final Button keyBindButton;

    public HeaderLine(ConfigScreen screen, FeatureState state) {
        super(true, state.feature.name, state.feature.tooltip);
        this.screen = screen;
        this.state = state;
        // X and Y are set by the parent layout
        this.toggleButton = CycleButton.builder(this::displayBool)
                .withValues(false, true)
                .withInitialValue(this.state.enabled)
                .displayOnlyValue()
                .create(0, 0, FeatureEntry.INPUT_WIDTH * 3 / 8, FeatureEntry.LINE_HEIGHT, this.state.feature.name, this::onToggle);
        this.keyBindButton = Button.builder(this.state.toggleKey.getDisplayName(), this::onKeyBind)
                .size(FeatureEntry.INPUT_WIDTH * 5 / 8, FeatureEntry.LINE_HEIGHT)
                .tooltip(KEY_BIND_TOOLTIP)
                .createNarration(this::narrateKeyBind)
                .build();
        this.horizontal.addChild(this.toggleButton);
        this.horizontal.addChild(this.keyBindButton);
        this.finish();
    }

    private Component displayBool(boolean value) {
        return value ? ON : OFF;
    }

    private MutableComponent narrateKeyBind(Supplier<MutableComponent> message) {
        return this.state.toggleKey == InputConstants.UNKNOWN
                ? Component.translatable("narrator.controls.unbound", this.state.feature.name)
                : Component.translatable("narrator.controls.bound", this.state.feature.name, message.get());
    }

    private void onToggle(CycleButton<Boolean> button, boolean value) {
        this.state.enabled = value;
    }

    private void onKeyBind(Button button) {
        button.setMessage(
                Component.literal("> ")
                        .append(button.getMessage().copy().withStyle(ChatFormatting.WHITE, ChatFormatting.UNDERLINE))
                        .append(" <")
                        .withStyle(ChatFormatting.YELLOW)
        );
        this.screen.awaitKeyBindFor(this.state.feature);
    }

    @Override
    public void refreshState() {
        this.toggleButton.setValue(this.state.enabled);
        this.keyBindButton.setMessage(this.state.toggleKey.getDisplayName());
    }
}
