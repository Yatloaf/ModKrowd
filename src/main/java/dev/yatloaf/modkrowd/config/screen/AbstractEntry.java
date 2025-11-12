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
        this.label = new TextWidget(0, 20, label, client.textRenderer);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        this.label.setWidth(this.getContentWidth() - 80);
        this.label.setPosition(this.getContentX(), this.getContentY());
        this.label.render(context, mouseX, mouseY, deltaTicks);
    }

    public abstract void refreshState();
}
