package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.config.Features;
import dev.yatloaf.modkrowd.cubekrowd.command.PreviewCommands;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class ChatScreenMixin extends Screen {
    // MESSAGE_PREVIEW

    protected ChatScreenMixin(Component title) {
        super(title);
    }

    // Clear even if the feature is disabled
    @Inject(method = "removed", at = @At("HEAD"))
    private void removedInject(CallbackInfo ci) {
        Features.MESSAGE_PREVIEW.clearPreviewMessageUnlessQueued();
    }

    // Clear even if the feature is disabled
    @Inject(method = "handleChatInput", at = @At("HEAD"))
    private void handleChatInputInject(String chatText, boolean addToHistory, CallbackInfo ci) {
        Features.MESSAGE_PREVIEW.queueClearPreviewMessage();
        if (Features.AUTOSWITCH.active) {
            Features.AUTOSWITCH.onSendMessage();
        }
    }

    @Inject(method = "onEdited", at = @At("TAIL"))
    private void onEditedInject(String chatText, CallbackInfo ci) {
        if (Features.MESSAGE_PREVIEW.active) {
            updatePreview(chatText);
        }
    }

    @Unique
    private static void updatePreview(String chatText) {
        Features.MESSAGE_PREVIEW.updatePreviewMessage(PreviewCommands.preview(StringUtils.normalizeSpace(chatText)));
    }
}
