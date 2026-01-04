package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.config.PredicateIndex;
import dev.yatloaf.modkrowd.cubekrowd.command.PreviewCommands;
import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.DirectMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.MessageCache;
import dev.yatloaf.modkrowd.mixinduck.GuiMessageLineDuck;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.ComponentRenderUtils;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

public class MessagePreviewFeature extends Feature {
    public static final GuiMessageTag PREVIEW_INDICATOR = new GuiMessageTag(
            CKColor.LIGHT_PURPLE.textColor.getValue(),
            null,
            Component.literal("Preview message"),
            "ModKrowd:Preview"
    );
    public static final int PREVIEW_BACKGROUND_TINT = 0x5F005F;

    public String replyTarget = null;

    private TextCache previewMessage = TextCache.EMPTY;
    private long removePreview = Long.MAX_VALUE;

    private TextCache linedMessage = null;
    private int linedWidth = 0;
    private List<GuiMessage.Line> previewMessageLines = List.of();

    public MessagePreviewFeature(String id, PredicateIndex allowedPredicates) {
        super(id, allowedPredicates);
    }

    @Override
    public void onDisable(Minecraft minecraft, ActionQueue queue) {
        this.replyTarget = null;
    }

    @Override
    public void onMessage(MessageCache message, Minecraft minecraft, ActionQueue queue) {
        if (!(minecraft.screen instanceof ChatScreen)) {
            this.clearPreviewMessage();
        }

        if (message.result() instanceof DirectMessage dm) {
            this.replyTarget = dm.other();
        }
    }

    @Override
    public void onEndTick(Minecraft minecraft, ActionQueue queue) {
        if (ModKrowd.tick >= this.removePreview) {
            this.clearPreviewMessage();
        }
    }

    public boolean hasPreviewMessage() {
        return this.previewMessage != TextCache.EMPTY;
    }

    public List<GuiMessage.Line> getPreviewMessageLines(int width, Font font) {
        if (this.linedMessage == this.previewMessage && this.linedWidth == width) {
            return this.previewMessageLines;
        }

        // Assume preview messages don't stay across subserver changes, so we don't have to care about those
        MessageCache cache = new MessageCache(this.previewMessage, ModKrowd.currentSubserver);
        cache.setBackgroundTint(PREVIEW_BACKGROUND_TINT);

        List<FormattedCharSequence> orderedTextLines = ComponentRenderUtils.wrapComponents(
                this.previewMessage.text(), width, font
        );

        List<GuiMessage.Line> lines = new ArrayList<>(orderedTextLines.size());
        for (int l = orderedTextLines.size() - 1; l >= 0; l--) {
            FormattedCharSequence currentLine = orderedTextLines.get(l);
            boolean endOfEntry = l == orderedTextLines.size() - 1;

            GuiMessage.Line line = new GuiMessage.Line(Integer.MIN_VALUE, currentLine, PREVIEW_INDICATOR, endOfEntry);
            ((GuiMessageLineDuck)(Object) line).modKrowd$setMessageCache(cache);
            lines.add(line);
        }

        this.linedMessage = this.previewMessage;
        this.linedWidth = width;
        this.previewMessageLines = lines;
        return lines;
    }

    public void updatePreviewMessage(TextCache message) {
        this.previewMessage = message;
        this.removePreview = Long.MAX_VALUE;
    }

    public void clearPreviewMessage() {
        this.previewMessage = TextCache.EMPTY;
        this.removePreview = Long.MAX_VALUE;
        PreviewCommands.advanceRandom();
    }

    public void queueClearPreviewMessage() {
        if (this.removePreview > ModKrowd.tick + 5) {
            this.removePreview = ModKrowd.tick + 5; // 250ms ping
        }
    }

    public void clearPreviewMessageUnlessQueued() {
        if (this.removePreview == Long.MAX_VALUE) {
            this.clearPreviewMessage();
        }
    }
}
