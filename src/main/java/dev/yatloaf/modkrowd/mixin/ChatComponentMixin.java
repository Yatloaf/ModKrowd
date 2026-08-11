package dev.yatloaf.modkrowd.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.config.Features;
import dev.yatloaf.modkrowd.cubekrowd.common.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.MessageCache;
import dev.yatloaf.modkrowd.mixinduck.ChatComponentDuck;
import dev.yatloaf.modkrowd.mixinduck.GuiMessageDuck;
import dev.yatloaf.modkrowd.util.ChainedListView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.client.multiplayer.chat.GuiMessageSource;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
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

    @Shadow @Final private Minecraft minecraft;
    @Shadow @Final private List<GuiMessage.Line> trimmedMessages;
    @Shadow private int chatScrollbarPos;
    @Shadow protected abstract void addMessage(Component contents, @Nullable MessageSignature signature, GuiMessageSource source, @Nullable GuiMessageTag tag);
    @Shadow protected abstract void logChatMessage(GuiMessage message);
    @Shadow protected abstract void addMessageToDisplayQueue(GuiMessage message);
    @Shadow protected abstract void addMessageToQueue(GuiMessage message);
    @Shadow public abstract void scrollChat(int dir);
    @Shadow protected abstract int getWidth();
    @Shadow protected abstract double getScale();
    @Shadow protected abstract int getLineHeight();


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
                return ((GuiMessageDuck)(Object) line.parent()).modKrowd$getMessageCache();
            }
        }
        return null;
    }

    @Override
    public void modKrowd$addMessage(Component contents, @Nullable MessageSignature signature, GuiMessageSource source, @Nullable GuiMessageTag tag) {
        this.addMessage(contents, signature, source, tag);
    }

    // Redirected visibleMessages
    @Unique private List<GuiMessage.Line> extendedMessages = this.trimmedMessages;

    // Insert preview message efficiently
    @Redirect(method = "forEachLine", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/gui/components/ChatComponent;trimmedMessages:Ljava/util/List;"))
    private List<GuiMessage.Line> forEachLine_trimmedMessagesRedirect(ChatComponent instance) {
        return this.extendedMessages;
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IILnet/minecraft/client/gui/components/ChatComponent$DisplayMode;)V", at = @At("HEAD"))
    private void extractRenderStateInject(ChatComponent.ChatGraphicsAccess graphics, int screenHeight, int ticks, ChatComponent.DisplayMode displayMode, CallbackInfo ci) {
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

    @Redirect(method = "extractRenderState(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IILnet/minecraft/client/gui/components/ChatComponent$DisplayMode;)V", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/gui/components/ChatComponent;trimmedMessages:Ljava/util/List;"))
    private List<GuiMessage.Line> render_trimmedMessagesRedirect(ChatComponent instance) {
        return this.extendedMessages;
    }

    // Lambda method! line is argsOnly due to being passed from outside
    // COMPAT: fi.dy.masa.tweakeroo.mixin.hud.MixinChatHud::tweakeroo_overrideChatBackgroundColor already does @Redirect,
    // avoid conflict by using @ModifyArg instead and potentially using Tweakeroo's result
    @ModifyArg(method = "lambda$extractRenderState$1", index = 4, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;fill(IIIII)V"))
    private static int fillArg(int color, @Local(argsOnly = true, name = "arg5") @NotNull GuiMessage.Line arg5) {
        // If any RGB bits are set, this was certainly modified by another mod. Don't mess with that
        if ((color & 0x00_FF_FF_FF) != 0) {
            return color;
        } else {
            GuiMessageDuck messageDuck = (GuiMessageDuck)(Object) arg5.parent();
            return color | messageDuck.modKrowd$getMessageCache().backgroundTint();
        }
    }

    @Inject(method = "clearMessages", cancellable = true, at = @At("HEAD"))
    private void clearMessagesInject(CallbackInfo ci) {
        if (Features.PERSISTENT_CHAT_HISTORY.active || Features.SEPARATE_CHAT_HISTORY.active) {
            ci.cancel();
        }
    }

    // Rewrite entire method to avoid two redirects and a field
    @Inject(method = "addMessage",
            at = @At("HEAD"), cancellable = true)
    public void addMessageInject(Component contents, MessageSignature signature, GuiMessageSource source, GuiMessageTag tag, CallbackInfo ci) {
        GuiMessage loggedLine = new GuiMessage(this.minecraft.gui.hud.getGuiTicks(), contents, signature, source, tag);
        this.logChatMessage(loggedLine);

        MessageCache messageCache = MessageCache.of(TextCache.of((MutableComponent) contents), ModKrowd.currentSubserver);
        ModKrowd.onMessage(messageCache);

        if (!messageCache.blocked()) {
            GuiMessage displayedLine = new GuiMessage(this.minecraft.gui.hud.getGuiTicks(), messageCache.themedOrDefault().text(), signature, source, tag);
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
    private Object addFirstArg(Object e) {
        GuiMessage.Line line = (GuiMessage.Line) e;
        GuiMessageDuck messageDuck = (GuiMessageDuck)(Object) line.parent();
        MessageCache cache = messageDuck.modKrowd$getMessageCache();

        cache.lines.add(line);

        return e;
    }
}
