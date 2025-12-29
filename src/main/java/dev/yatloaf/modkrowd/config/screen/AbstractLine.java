package dev.yatloaf.modkrowd.config.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.network.chat.Component;

public abstract class AbstractLine {
    public final boolean header;
    public final LinearLayout horizontal;

    public final StringWidget labelWidget;

    public AbstractLine(boolean header, Component label, Tooltip tooltip) {
        this.header = header;
        this.horizontal = LinearLayout.horizontal();

        int leftEdgeWidth = header ? FeatureEntry.LEFT_EDGE_WIDTH_HEADER : FeatureEntry.LEFT_EDGE_WIDTH_SUB;
        // X and Y are set by the parent layout
        SpacerElement leftEdge = new SpacerElement(leftEdgeWidth, FeatureEntry.LINE_HEIGHT);
        this.horizontal.addChild(leftEdge);

        // X and Y are set by the parent layout, Width is set manually
        this.labelWidget = new StringWidget(0, FeatureEntry.LINE_HEIGHT, label, Minecraft.getInstance().font);
        if (tooltip != null) {
            this.labelWidget.setTooltip(tooltip);
        }
        this.horizontal.addChild(this.labelWidget);
    }

    protected void finish() {
        // X and Y are set by the parent layout
        SpacerElement rightEdge = new SpacerElement(FeatureEntry.RIGHT_EDGE_WIDTH, FeatureEntry.LINE_HEIGHT);
        this.horizontal.addChild(rightEdge);
    }

    public abstract void refreshState();
}
