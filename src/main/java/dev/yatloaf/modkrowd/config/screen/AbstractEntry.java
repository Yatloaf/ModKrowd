package dev.yatloaf.modkrowd.config.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

public abstract class AbstractEntry extends ElementListWidget.Entry<AbstractEntry> {
    protected final MinecraftClient client;
    public final TextWidget label;

    public AbstractEntry(MinecraftClient client, Text label) {
        this.client = client;
        this.label = new TextWidget(0, 20, label, client.textRenderer).alignLeft();
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        this.label.setWidth(entryWidth - 80);
        this.label.setPosition(x, y);
        this.label.render(context, mouseX, mouseY, tickDelta);
    }

    public abstract void refreshState();
}
