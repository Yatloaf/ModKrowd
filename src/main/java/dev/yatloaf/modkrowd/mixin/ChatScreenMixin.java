package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.config.Features;
import dev.yatloaf.modkrowd.config.screen.MessageCopyScreen;
import dev.yatloaf.modkrowd.cubekrowd.command.PreviewCommands;
import dev.yatloaf.modkrowd.cubekrowd.message.MessageCache;
import dev.yatloaf.modkrowd.mixinduck.ChatComponentDuck;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    @Inject(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseClicked(Lnet/minecraft/client/input/MouseButtonEvent;Z)Z"))
    private void mouseClickedInject(MouseButtonEvent mouseButtonEvent, boolean bl, CallbackInfoReturnable<Boolean> cir) {
        if (Features.MESSAGE_COPY.active && mouseButtonEvent.button() == 1) {
            ChatComponentDuck chat = (ChatComponentDuck) this.minecraft.gui.getChat();
            MessageCache message = chat.modKrowd$getMessageAt(mouseButtonEvent.x(), mouseButtonEvent.y());
            if (message != null) {
                this.minecraft.setScreen(new MessageCopyScreen(message));
            }
        }
    }

    @Unique
    private static void updatePreview(String chatText) {
        Features.MESSAGE_PREVIEW.updatePreviewMessage(PreviewCommands.preview(StringUtils.normalizeSpace(chatText)));
    }
}
