package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.config.feature.Feature;
import dev.yatloaf.modkrowd.cubekrowd.command.PreviewCommands;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public class ChatScreenMixin extends Screen {
    // MESSAGE_PREVIEW

    @Shadow protected EditBox input;

    protected ChatScreenMixin(Component title) {
        super(title);
    }

    // Clear even if the feature is disabled
    @Inject(method = "removed", at = @At("HEAD"))
    private void removedInject(CallbackInfo ci) {
        ModKrowd.CONFIG.MESSAGE_PREVIEW.clearPreviewMessageUnlessQueued();
    }

    // Handle quick toggle key
    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void keyPressedInject(KeyEvent input, CallbackInfoReturnable<Boolean> cir) {
        if (ModKrowd.INIT && ModKrowd.TOGGLE_MESSAGE_PREVIEW_KEY.matches(input)) {
            Feature f = ModKrowd.CONFIG.MESSAGE_PREVIEW;
            // Next predicate
            f.predicate = f.allowedPredicates.index.get((f.allowedPredicates.index.indexOf(f.predicate) + 1) % f.allowedPredicates.index.size());
            ModKrowd.CONFIG.updateFeatures();
            if (f.enabled) {
                updatePreview(this.input.getValue());
            }
        }
    }

    // Clear even if the feature is disabled
    @Inject(method = "handleChatInput", at = @At("HEAD"))
    private void handleChatInputInject(String chatText, boolean addToHistory, CallbackInfo ci) {
        ModKrowd.CONFIG.MESSAGE_PREVIEW.queueClearPreviewMessage();
    }

    @Inject(method = "onEdited", at = @At("TAIL"))
    private void onEditedInject(String chatText, CallbackInfo ci) {
        if (ModKrowd.CONFIG.MESSAGE_PREVIEW.enabled) {
            updatePreview(chatText);
        }
    }

    @Unique
    private static void updatePreview(String chatText) {
        ModKrowd.CONFIG.MESSAGE_PREVIEW.updatePreviewMessage(PreviewCommands.preview(StringUtils.normalizeSpace(chatText)));
    }
}
