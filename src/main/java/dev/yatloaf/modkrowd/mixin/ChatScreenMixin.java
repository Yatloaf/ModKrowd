package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.config.feature.Feature;
import dev.yatloaf.modkrowd.cubekrowd.command.PreviewCommands;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
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

    @Shadow protected TextFieldWidget chatField;

    protected ChatScreenMixin(Text title) {
        super(title);
    }

    // Clear even if the feature is disabled
    @Inject(method = "removed", at = @At("HEAD"))
    private void removedInject(CallbackInfo ci) {
        ModKrowd.CONFIG.MESSAGE_PREVIEW.clearPreviewMessageUnlessQueued();
    }

    // Handle quick toggle key
    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void keyPressedInject(KeyInput input, CallbackInfoReturnable<Boolean> cir) {
        if (ModKrowd.INIT && ModKrowd.TOGGLE_MESSAGE_PREVIEW_KEY.matchesKey(input)) {
            Feature f = ModKrowd.CONFIG.MESSAGE_PREVIEW;
            // Next predicate
            f.predicate = f.allowedPredicates.index.get((f.allowedPredicates.index.indexOf(f.predicate) + 1) % f.allowedPredicates.index.size());
            ModKrowd.CONFIG.updateFeatures();
            if (f.enabled) {
                updatePreview(this.chatField.getText());
            }
        }
    }

    // Clear even if the feature is disabled
    @Inject(method = "sendMessage", at = @At("HEAD"))
    private void sendMessageInject(String chatText, boolean addToHistory, CallbackInfo ci) {
        ModKrowd.CONFIG.MESSAGE_PREVIEW.queueClearPreviewMessage();
    }

    @Inject(method = "onChatFieldUpdate", at = @At("TAIL"))
    private void onChatFieldUpdateInject(String chatText, CallbackInfo ci) {
        if (ModKrowd.CONFIG.MESSAGE_PREVIEW.enabled) {
            updatePreview(chatText);
        }
    }

    @Unique
    private static void updatePreview(String chatText) {
        ModKrowd.CONFIG.MESSAGE_PREVIEW.updatePreviewMessage(PreviewCommands.preview(StringUtils.normalizeSpace(chatText)));
    }
}
