package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.MessageCache;
import dev.yatloaf.modkrowd.util.ChainedListView;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
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
    // Also: any message modification

    @Shadow @Final private MinecraftClient client;
    @Shadow @Final private List<ChatHudLine.Visible> visibleMessages;
    @Shadow public abstract int getWidth();
    @Shadow public abstract double getChatScale();
    @Shadow protected abstract void logChatMessage(ChatHudLine message);
    @Shadow protected abstract void addVisibleMessage(ChatHudLine message);
    @Shadow protected abstract void addMessage(ChatHudLine message);

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
        if (ModKrowd.CONFIG.DEJOIN.enabled || ModKrowd.CONFIG.SEPARATE_CHAT_HISTORY.enabled) {
            ci.cancel();
        }
    }

    // Rewrite entire method to avoid two redirects and a field
    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
            at = @At("HEAD"), cancellable = true)
    public void addMessageInject(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {
        ChatHudLine loggedLine = new ChatHudLine(this.client.inGameHud.getTicks(), message, signatureData, indicator);
        this.logChatMessage(loggedLine);

        MessageCache messageCache = MessageCache.of(TextCache.of((MutableText) message), ModKrowd.currentSubserver);
        ModKrowd.onMessage(messageCache);

        if (!messageCache.blocked()) {
            ChatHudLine displayedLine = new ChatHudLine(this.client.inGameHud.getTicks(), messageCache.themedOrDefault().text(), signatureData, indicator);
            this.addVisibleMessage(displayedLine);
            this.addMessage(displayedLine);
        }

        ci.cancel();
    }
}
