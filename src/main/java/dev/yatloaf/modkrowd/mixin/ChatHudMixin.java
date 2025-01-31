package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.util.ChainedListView;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
    // DEJOIN
    // SEPARATE_CHAT_HISTORY
    // MESSAGE_PREVIEW

    @Shadow @Final private MinecraftClient client;
    @Shadow @Final private List<ChatHudLine.Visible> visibleMessages;
    @Shadow public abstract int getWidth();
    @Shadow public abstract double getChatScale();

    // Redirected visibleMessages
    @Unique private List<ChatHudLine.Visible> extendedMessages = this.visibleMessages;

    // Insert preview message efficiently
    @Inject(method = "render", at = @At("HEAD"))
    private void renderInject(DrawContext context, int currentTick, int mouseX, int mouseY, boolean focused, CallbackInfo ci) {
        if (ModKrowd.CONFIG.MESSAGE_PREVIEW.enabled && ModKrowd.CONFIG.MESSAGE_PREVIEW.hasPreviewMessage()) {
            int width = MathHelper.floor((double) this.getWidth() / this.getChatScale());
            List<ChatHudLine.Visible> previewMessageLines = ModKrowd.CONFIG.MESSAGE_PREVIEW.getPreviewMessageLines(width, this.client.textRenderer);
            this.extendedMessages = new ChainedListView<>(previewMessageLines, this.visibleMessages);
        } else {
            this.extendedMessages = this.visibleMessages;
        }
    }

    @Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/ChatHud;visibleMessages:Ljava/util/List;"))
    private List<ChatHudLine.Visible> visibleMessagesRedirect(ChatHud instance) {
        return this.extendedMessages;
    }

    @Inject(method = "clear", cancellable = true, at = @At("HEAD"))
    private void clearInject(CallbackInfo ci) {
        if (ModKrowd.CONFIG.DEJOIN.enabled || ModKrowd.CONFIG.SEPERATE_CHAT_HISTORY.enabled) {
            ci.cancel();
        }
    }
}
