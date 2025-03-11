package dev.yatloaf.modkrowd.config.feature;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.config.ActionQueue;
import dev.yatloaf.modkrowd.config.PredicateIndex;
import dev.yatloaf.modkrowd.cubekrowd.message.DirectMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.CubeKrowdMessageCache;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.MessageCache;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.util.ChatMessages;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MessagePreviewFeature extends Feature {

    private static final Text PREVIEW_TEXT = Text.literal("Preview message");
    private static final int PREVIEW_COLOR = Objects.requireNonNull(Formatting.LIGHT_PURPLE.getColorValue());
    public static final MessageIndicator PREVIEW_INDICATOR = new MessageIndicator(PREVIEW_COLOR, null, PREVIEW_TEXT, "ModKrowd:Preview");
    private static final OrderedText PREVIEW_PREFIX = OrderedText.styled(' ', Style.EMPTY);

    public String replyTarget = null;

    private TextCache previewMessage = TextCache.EMPTY;
    private long removePreview = Long.MAX_VALUE;

    private TextCache linedMessage = null;
    private int linedWidth = 0;
    private List<ChatHudLine.Visible> previewMessageLines = List.of();

    public MessagePreviewFeature(String id, PredicateIndex allowedPredicates) {
        super(id, allowedPredicates);
    }

    @Override
    public void onDisable(MinecraftClient client, ActionQueue queue) {
        this.replyTarget = null;
    }

    @Override
    public void onMessage(MessageCache message, boolean overlay, MinecraftClient client, ActionQueue queue) {
        if (!(client.currentScreen instanceof ChatScreen)) {
            this.clearPreviewMessage();
        }
        if (message instanceof CubeKrowdMessageCache ckMessage) {
            DirectMessage dm = ckMessage.directMessageFast();
            if (dm.isReal()) {
                this.replyTarget = dm.other();
            }
        }
    }

    @Override
    public void onEndTick(MinecraftClient client, ActionQueue queue) {
        if (ModKrowd.tick >= this.removePreview) {
            this.clearPreviewMessage();
        }
    }

    public boolean hasPreviewMessage() {
        return this.previewMessage != TextCache.EMPTY;
    }

    public List<ChatHudLine.Visible> getPreviewMessageLines(int width, TextRenderer textRenderer) {
        if (this.linedMessage == this.previewMessage && this.linedWidth == width) {
            return this.previewMessageLines;
        }

        List<OrderedText> orderedTextLines = ChatMessages.breakRenderedChatMessageLines(
                this.previewMessage.text(), width, textRenderer
        );

        List<ChatHudLine.Visible> visibleLines = new ArrayList<>(orderedTextLines.size());
        for (int l = orderedTextLines.size() - 1; l >= 0; l--) {
            OrderedText currentLine = OrderedText.concat(PREVIEW_PREFIX, orderedTextLines.get(l));
            boolean endOfEntry = l == orderedTextLines.size() - 1;
            visibleLines.add(new ChatHudLine.Visible(
                    Integer.MIN_VALUE, currentLine, PREVIEW_INDICATOR, endOfEntry
            ));
        }

        this.linedMessage = this.previewMessage;
        this.linedWidth = width;
        this.previewMessageLines = visibleLines;
        return visibleLines;
    }

    public void updatePreviewMessage(TextCache message) {
        this.previewMessage = message;
        this.removePreview = Long.MAX_VALUE;
    }

    public void clearPreviewMessage() {
        this.previewMessage = TextCache.EMPTY;
        this.removePreview = Long.MAX_VALUE;
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
