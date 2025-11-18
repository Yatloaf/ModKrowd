package dev.yatloaf.modkrowd.config.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;

public abstract class AbstractEntry extends ContainerObjectSelectionList.Entry<AbstractEntry> {
    protected final Minecraft minecraft;
    public final StringWidget label;

    public AbstractEntry(Minecraft minecraft, Component label) {
        this.minecraft = minecraft;
        this.label = new StringWidget(0, 20, label, minecraft.font);
    }

    @Override
    public void renderContent(GuiGraphics context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        this.label.setWidth(this.getContentWidth() - 80);
        this.label.setPosition(this.getContentX(), this.getContentY());
        this.label.render(context, mouseX, mouseY, deltaTicks);
    }

    public abstract void refreshState();
}
