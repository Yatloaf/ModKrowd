package dev.yatloaf.modkrowd.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.config.Features;
import dev.yatloaf.modkrowd.cubekrowd.common.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.MessageCache;
import dev.yatloaf.modkrowd.mixinduck.ChatComponentDuck;
import dev.yatloaf.modkrowd.mixinduck.GuiMessageDuck;
import dev.yatloaf.modkrowd.mixinduck.GuiMessageLineDuck;
import dev.yatloaf.modkrowd.util.ChainedListView;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
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
public abstract class ChatComponentMixin implements ChatComponentDuck {
    // DEJOIN
    // SEPARATE_CHAT_HISTORY
    // MESSAGE_COPY
    // MESSAGE_PREVIEW
    // Also: any message modification

    @Shadow @Final Minecraft minecraft;
    @Shadow @Final private List<GuiMessage.Line> trimmedMessages;
    @Shadow protected abstract int getWidth();
    @Shadow protected abstract double getScale();
    @Shadow protected abstract void logChatMessage(GuiMessage message);
    @Shadow protected abstract void addMessageToDisplayQueue(GuiMessage message);
    @Shadow protected abstract void addMessageToQueue(GuiMessage message);
    @Shadow public abstract void scrollChat(int scroll);
    @Shadow protected abstract int getLineHeight();

    @Shadow
    private int chatScrollbarPos;

    @Override
    public MessageCache modKrowd$getMessageAt(double x, double y) {
        // Reverse the calculations manually instead of somehow using ChatGraphicsAccess and ActiveTextCollector
        double scale = this.getScale();
        int scaledWidth = Mth.floor(this.getWidth() / scale);
        if (x >= -4 && x < scaledWidth + 4 + 4) {
            int guiScaledHeight = this.minecraft.getWindow().getGuiScaledHeight();
            int chatBottom = Mth.floor((guiScaledHeight - 40) / scale);
            int index = Mth.floor((chatBottom - y) / this.getLineHeight()) + this.chatScrollbarPos;
            if (index >= 0 && index < this.extendedMessages.size()) {
                GuiMessage.Line line = this.extendedMessages.get(index);
                return ((GuiMessageLineDuck)(Object) line).modKrowd$getMessageCache();
            }
        }
        return null;
    }

    // Redirected visibleMessages
    @Unique private List<GuiMessage.Line> extendedMessages = this.trimmedMessages;

    // Insert preview message efficiently
    @Redirect(method = "forEachLine", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/gui/components/ChatComponent;trimmedMessages:Ljava/util/List;"))
    private List<GuiMessage.Line> forEachLine_trimmedMessagesRedirect(ChatComponent instance) {
        return this.extendedMessages;
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IIZ)V", at = @At("HEAD"))
    private void renderInject(ChatComponent.ChatGraphicsAccess chatGraphicsAccess, int i, int j, boolean bl, CallbackInfo ci) {
        if (Features.MESSAGE_PREVIEW.active && Features.MESSAGE_PREVIEW.hasPreviewMessage()) {
            int width = Mth.floor((double) this.getWidth() / this.getScale());
            List<GuiMessage.Line> previewMessageLines = Features.MESSAGE_PREVIEW.getPreviewMessageLines(width, this.minecraft.font);
            this.extendedMessages = new ChainedListView<>(previewMessageLines, this.trimmedMessages);
        } else {
            this.extendedMessages = this.trimmedMessages;
        }
        // Scroll back if the user scrolled further than possible
        this.scrollChat(0);
    }

    @Redirect(method = "render(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IIZ)V", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/gui/components/ChatComponent;trimmedMessages:Ljava/util/List;"))
    private List<GuiMessage.Line> render_trimmedMessagesRedirect(ChatComponent instance) {
        return this.extendedMessages;
    }

    // Lambda method! line is argsOnly due to being passed from outside
    @Redirect(method = "method_75802", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ARGB;black(F)I"))
    private static int blackArg(float alpha, @Local(argsOnly = true) @NotNull GuiMessage.Line line) {
        GuiMessageLineDuck lineDuck = (GuiMessageLineDuck)(Object) line;
        return ARGB.black(alpha) | lineDuck.modKrowd$getMessageCache().backgroundTint();
    }

    @Inject(method = "clearMessages", cancellable = true, at = @At("HEAD"))
    private void clearMessagesInject(CallbackInfo ci) {
        if (Features.PERSISTENT_CHAT_HISTORY.active || Features.SEPARATE_CHAT_HISTORY.active) {
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
            ((GuiMessageDuck)(Object) displayedLine).modKrowd$setMessageCache(messageCache);
            this.addMessageToDisplayQueue(displayedLine);
            this.addMessageToQueue(displayedLine);
        }

        ci.cancel();
    }

    @Inject(method = "addMessageToDisplayQueue", at = @At("HEAD"))
    private void addMessageToDisplayQueueInject(GuiMessage message, CallbackInfo ci) {
        GuiMessageDuck messageDuck = (GuiMessageDuck)(Object) message;
        MessageCache cache = messageDuck.modKrowd$getMessageCache();
        cache.lines.clear();
    }

    @ModifyArg(method = "addMessageToDisplayQueue", at = @At(value = "INVOKE", target = "Ljava/util/List;addFirst(Ljava/lang/Object;)V"))
    private Object addFirstArg(Object e, @Local(argsOnly = true) @NotNull GuiMessage message) {
        GuiMessageDuck messageDuck = (GuiMessageDuck)(Object) message;
        // We would cast to GuiMessage.Line to undo type erasure, but this has to be a double cast anyway
        GuiMessageLineDuck lineDuck = (GuiMessageLineDuck) e;
        MessageCache cache = messageDuck.modKrowd$getMessageCache();
        GuiMessage.Line line = (GuiMessage.Line) e;

        lineDuck.modKrowd$setMessageCache(cache);
        cache.lines.add(line);

        return lineDuck;
    }
}
