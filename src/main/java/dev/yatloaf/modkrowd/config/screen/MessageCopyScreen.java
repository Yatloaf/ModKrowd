package dev.yatloaf.modkrowd.config.screen;

import com.mojang.serialization.JsonOps;
import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.message.cache.MessageCache;
import dev.yatloaf.modkrowd.util.text.StyledString;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.client.GuiMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

public class MessageCopyScreen extends Screen {
    private static final Format[] FORMATS = {
            new Format(
                    Component.translatable("modkrowd.message_copy.format.unformatted"),
                    Component::getString),
            new Format(
                    Component.translatable("modkrowd.message_copy.format.legacy"),
                    component -> StyledString.fromText(component).toFormattedString("§")
            ),
            new Format(
                    Component.translatable("modkrowd.message_copy.format.legacy_ampersand"),
                    component -> StyledString.fromText(component).toFormattedString("&")
            ),
            new Format(
                    Component.translatable("modkrowd.message_copy.format.minimessage"),
                    MessageCopyScreen::vanillaToMiniMessage
            ),
            new Format(
                    Component.translatable("modkrowd.message_copy.format.json"),
                    component -> ComponentSerialization.CODEC
                            .encodeStart(JsonOps.INSTANCE, component)
                            .getOrThrow()
                            .toString()
            ),
            new Format(
                    Component.translatable("modkrowd.message_copy.format.nbt"),
                    component -> ComponentSerialization.CODEC
                            .encodeStart(NbtOps.INSTANCE, component)
                            .getOrThrow()
                            .toString()
            )
    };

    private final MessageCache cache;
    public final Component component;
    public final List<GuiMessage.Line> lines;
    private final Minecraft minecraft;
    private final GridLayout grid;
    private final GridLayout.RowHelper row;
    private final int gridWidth;
    private final int gridHeight;

    public MessageCopyScreen(MessageCache message) {
        super(Component.translatable("modkrowd.message_copy.title"));
        this.cache = message;
        this.component = stripDisallowedClickEvents(message.original.text());
        this.lines = message.lines;
        this.minecraft = Minecraft.getInstance();
        this.grid = new GridLayout();
        this.row = this.grid.createRowHelper(3);

        this.createElementsFromFormats(FORMATS);

        this.grid.arrangeElements();
        this.gridWidth = this.grid.getWidth();
        this.gridHeight = this.grid.getHeight();
    }

    private void createElementsFromFormats(Format[] formats) {
        // First figure out the maximum name length
        int maxFormatWidth = 0;
        for (Format format : formats) {
            int formatWidth = this.minecraft.font.width(format.name);
            if (formatWidth > maxFormatWidth) {
                maxFormatWidth = formatWidth;
            }
        }
        // Kind of messy
        for (Format format : formats) {
            String encoded;

            StringWidget title = new StringWidget(maxFormatWidth, 20, format.name, this.minecraft.font);
            EditBox preview = new EditBox(this.minecraft.font, 256, 20, Component.empty());
            Button copyButton;

            preview.setMaxLength(0x100_000);
            preview.setEditable(false);

            try {
                encoded = format.formatter.apply(this.component);

                preview.setValue(encoded);
                preview.setTextColorUneditable(CKColor.GRAY.textColor.getValue() | 0xFF_00_00_00);
            } catch (Exception e) {
                encoded = e.getMessage();

                preview.setValue(Component.translatable("modkrowd.message_copy.conversion_error", encoded).getString());
                preview.setTextColorUneditable(CKColor.RED.textColor.getValue() | 0xFF_00_00_00);
            }

            String clipboard = encoded;
            copyButton = Button.builder(Component.translatable("modkrowd.message_copy.copy"), b ->
                            this.minecraft.keyboardHandler
                            .setClipboard(clipboard))
                            .size(64, 20).build();

            this.row.addChild(title);
            this.row.addChild(preview);
            this.row.addChild(copyButton);
        }
    }

    @Override
    protected void init() {
        this.grid.spacing(4);
        this.grid.setPosition((this.width - this.gridWidth) / 2, (this.height - this.gridHeight) / 2);
        this.grid.arrangeElements();
        this.grid.visitWidgets(this::addRenderableWidget);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);

        double width = ChatComponent.getWidth(this.minecraft.options.chatWidth().get());
        double scale = this.minecraft.options.chatScale().get();
        double spacing = this.minecraft.options.chatLineSpacing().get();

        int lineHeight = (int) (9.0 * (spacing + 1.0));
        int scaledWidth = Mth.floor(width / scale);

        int x = (this.width - Mth.floor(width / scale)) / 2;
        int y = this.grid.getY() - this.lines.size() * lineHeight - 11;
        int textY = y + lineHeight - (int) Math.round(8.0 * (spacing + 1.0) - 4.0 * spacing);

        guiGraphics.drawCenteredString(
                this.minecraft.font,
                Component.translatable("modkrowd.message_copy.title"),
                this.width / 2,
                y - lineHeight - 11,
                0xFF_FF_FF_FF
        );

        guiGraphics.fill(
                x - 4,
                y,
                x + scaledWidth + 4 + 4,
                y + lineHeight * this.lines.size(),
                0x7F_00_00_00 | this.cache.backgroundTint()
        );

        for (GuiMessage.Line line : this.lines) {
            guiGraphics.drawString(
                    this.minecraft.font,
                    line.content(),
                    x,
                    textY,
                    0xFF_FF_FF_FF
            );

            textY += lineHeight;
        }
    }

    public record Format(Component name, Function<Component, String> formatter) {}

    // Encoding into certain formats will fail if we don't strip click events disallowed from servers
    private static Component stripDisallowedClickEvents(Component component) {
        ComponentContents contents = component.getContents();
        ComponentContents resultContents;

        if (contents instanceof TranslatableContents translatable) {
            Object[] args = translatable.getArgs();
            Object[] newArgs = new Object[args.length];

            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Component compArg) {
                    newArgs[i] = stripDisallowedClickEvents(compArg);
                } else {
                    newArgs[i] = args[i];
                }
            }

            resultContents = new TranslatableContents(
                    translatable.getKey(),
                    translatable.getFallback(),
                    newArgs
            );
        } else {
            resultContents = contents;
        }
        MutableComponent result = MutableComponent.create(resultContents);

        Style style = component.getStyle();
        Style resultStyle;

        ClickEvent clickEvent = style.getClickEvent();

        if (clickEvent != null && !clickEvent.action().isAllowedFromServer()) {
            resultStyle = style.withClickEvent(null);
        } else {
            resultStyle = style;
        }

        result.setStyle(resultStyle);

        for (Component sibling : component.getSiblings()) {
            result.append(stripDisallowedClickEvents(sibling));
        }

        return result;
    }

    private static String vanillaToMiniMessage(Component component) {
        String json = ComponentSerialization.CODEC
                .encodeStart(JsonOps.INSTANCE, component)
                .getOrThrow()
                .toString();

        // Java is silly and gets confused if there are conflicting class names, hence the long-winded variable type
        net.kyori.adventure.text.Component adventureComponent = GsonComponentSerializer.gson().deserialize(json);

        return MiniMessage.miniMessage().serialize(adventureComponent);
    }
}
