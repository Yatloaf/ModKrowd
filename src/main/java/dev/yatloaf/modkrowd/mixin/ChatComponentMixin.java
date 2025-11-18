package dev.yatloaf.modkrowd.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.MessageCache;
import dev.yatloaf.modkrowd.mixinduck.ChatHudLineDuck;
import dev.yatloaf.modkrowd.util.ChainedListView;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChatComponent.class)
public abstract class ChatComponentMixin {
    // DEJOIN
    // SEPARATE_CHAT_HISTORY
    // MESSAGE_PREVIEW
    // Also: any message modification

    @Shadow @Final private Minecraft minecraft;
    @Shadow @Final private List<GuiMessage.Line> trimmedMessages;
    @Shadow public abstract int getWidth();
    @Shadow public abstract double getScale();
    @Shadow protected abstract void logChatMessage(GuiMessage message);
    @Shadow protected abstract void addMessageToDisplayQueue(GuiMessage message);
    @Shadow protected abstract void addMessageToQueue(GuiMessage message);
    @Shadow public abstract void scrollChat(int scroll);

    // Redirected visibleMessages
    @Unique private List<GuiMessage.Line> extendedMessages = this.trimmedMessages;

    // Insert preview message efficiently
    @Inject(method = "render", at = @At("HEAD"))
    private void renderInject(GuiGraphics context, int currentTick, int mouseX, int mouseY, boolean focused, CallbackInfo ci) {
        if (ModKrowd.CONFIG.MESSAGE_PREVIEW.enabled && ModKrowd.CONFIG.MESSAGE_PREVIEW.hasPreviewMessage()) {
            int width = Mth.floor((double) this.getWidth() / this.getScale());
            List<GuiMessage.Line> previewMessageLines = ModKrowd.CONFIG.MESSAGE_PREVIEW.getPreviewMessageLines(width, this.minecraft.font);
            this.extendedMessages = new ChainedListView<>(previewMessageLines, this.trimmedMessages);
        } else {
            this.extendedMessages = this.trimmedMessages;
        }
        // Scroll back if the user scrolled further than possible
        this.scrollChat(0);
    }

    @Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/ChatComponent;trimmedMessages:Ljava/util/List;"))
    private List<GuiMessage.Line> render_trimmedMessagesRedirect(ChatComponent instance) {
        return this.extendedMessages;
    }

    @Redirect(method = "forEachLine", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/ChatComponent;trimmedMessages:Ljava/util/List;"))
    private List<GuiMessage.Line> forEachLine_trimmedMessagesRedirect(ChatComponent instance) {
        return this.extendedMessages;
    }

    // Lambda method! line is argsOnly due to being passed from outside
    @ModifyArg(method = "method_71992", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ARGB;color(FI)I", ordinal = 0))
    private int withAlphaArg(int color, @Local(argsOnly = true) @NotNull GuiMessage.Line line) {
        ChatHudLineDuck lineDuck = (ChatHudLineDuck)(Object) line;
        return color | lineDuck.modKrowd$getBackgroundTint();
    }

    @Inject(method = "clearMessages", cancellable = true, at = @At("HEAD"))
    private void clearMessagesInject(CallbackInfo ci) {
        if (ModKrowd.CONFIG.DEJOIN.enabled || ModKrowd.CONFIG.SEPARATE_CHAT_HISTORY.enabled) {
            ci.cancel();
        }
    }

    // Rewrite entire method to avoid two redirects and a field
    @Inject(method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/GuiMessageTag;)V",
            at = @At("HEAD"), cancellable = true)
    public void addMessageInject(Component message, MessageSignature signatureData, GuiMessageTag indicator, CallbackInfo ci) {
        GuiMessage loggedLine = new GuiMessage(this.minecraft.gui.getGuiTicks(), message, signatureData, indicator);
        this.logChatMessage(loggedLine);

        MessageCache messageCache = MessageCache.of(TextCache.of((MutableComponent) message), ModKrowd.currentSubserver);
        ModKrowd.onMessage(messageCache);

        if (!messageCache.blocked()) {
            GuiMessage displayedLine = new GuiMessage(this.minecraft.gui.getGuiTicks(), messageCache.themedOrDefault().text(), signatureData, indicator);
            ((ChatHudLineDuck)(Object) displayedLine).modKrowd$setBackgroundTint(messageCache.backgroundTint());
            this.addMessageToDisplayQueue(displayedLine);
            this.addMessageToQueue(displayedLine);
        }

        ci.cancel();
    }

    @ModifyArg(method = "addMessageToDisplayQueue", at = @At(value = "INVOKE", target = "Ljava/util/List;add(ILjava/lang/Object;)V"))
    private Object addArg(Object t, @Local(argsOnly = true) @NotNull GuiMessage message) {
        ChatHudLineDuck messageDuck = (ChatHudLineDuck)(Object) message;
        // We would cast to ChatHudLine.Visible to undo type erasure, but this has to be a double cast anyway
        ChatHudLineDuck visibleDuck = (ChatHudLineDuck) t;
        visibleDuck.modKrowd$setBackgroundTint(messageDuck.modKrowd$getBackgroundTint());
        return visibleDuck;
    }

    @Redirect(method = "getClickedComponentStyleAt", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/ChatComponent;trimmedMessages:Ljava/util/List;"))
    private List<GuiMessage.Line> getClickedComponentStyleAt_trimmedMessagesRedirect(ChatComponent instance) {
        return this.extendedMessages;
    }

    @Redirect(method = "getMessageTagAt", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/ChatComponent;trimmedMessages:Ljava/util/List;"))
    private List<GuiMessage.Line> getMessageTagAt_trimmedMessagesRedirect(ChatComponent instance) {
        return this.extendedMessages;
    }

    @Redirect(method = "getMessageEndIndexAt", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/ChatComponent;trimmedMessages:Ljava/util/List;"))
    private List<GuiMessage.Line> getMessageEndIndexAt_trimmedMessagesRedirect(ChatComponent instance) {
        return this.extendedMessages;
    }

    @Redirect(method = "getMessageLineIndexAt", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/ChatComponent;trimmedMessages:Ljava/util/List;"))
    private List<GuiMessage.Line> getMessageLineIndexAt_trimmedMessagesRedirect(ChatComponent instance) {
        return this.extendedMessages;
    }
}
