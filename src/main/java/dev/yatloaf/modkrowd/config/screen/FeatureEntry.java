package dev.yatloaf.modkrowd.config.screen;

import dev.yatloaf.modkrowd.config.FeatureState;
import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class FeatureEntry extends ContainerObjectSelectionList.Entry<FeatureEntry> implements Layout {
    public static final int TREE_COLOR = CKColor.GRAY.textColor.getValue() | 0xFF_00_00_00;
    public static final int HOVER_COLOR = 0x3F_9F_9F_9F;

    public static final int LEFT_EDGE_WIDTH_HEADER = 4;
    public static final int LEFT_EDGE_WIDTH_SUB = 14;
    public static final int INPUT_WIDTH = 128;
    public static final int RIGHT_EDGE_WIDTH = 4;
    public static final int LINE_HEIGHT = 20;

    private final LinearLayout vertical;
    private final List<AbstractLine> lines = new ArrayList<>();
    private final List<Renderable> renderables = new ArrayList<>();
    private final List<NarratableEntry> narratables = new ArrayList<>();
    private final List<GuiEventListener> listeners = new ArrayList<>();

    public FeatureEntry(ConfigScreen screen, FeatureState featureState) {
        this.vertical = LinearLayout.vertical();
        this.addLine(new HeaderLine(screen, featureState));
        featureState.addOptions(this);
    }

    public void addBoolean(Component label, Tooltip tooltip, boolean startValue, BooleanSupplier getter, BooleanConsumer setter) {
        this.addLine(new BooleanLine(label, tooltip, startValue, getter, setter));
    }

    public void addInt(Component label, Tooltip tooltip, int startValue, int minValue, int maxValue, IntSupplier getter, IntConsumer setter) {
        this.addLine(new IntLine(label, tooltip, startValue, minValue, maxValue, getter, setter));
    }

    public void addDouble(Component label, Tooltip tooltip, double startValue, double minValue, double maxValue, DoubleSupplier getter, DoubleConsumer setter) {
        this.addLine(new DoubleLine(label, tooltip, startValue, minValue, maxValue, getter, setter));
    }

    public void addIdentifier(Component label, Tooltip tooltip, ResourceLocation startValue, Supplier<ResourceLocation> getter, Consumer<ResourceLocation> setter) {
        this.addLine(new IdentifierLine(label, tooltip, startValue, getter, setter));
    }

    private void addLine(AbstractLine line) {
        this.vertical.addChild(line.horizontal);
        line.horizontal.visitChildren(child -> {
            if (child instanceof Renderable renderable) {
                this.renderables.add(renderable);
            }
            if (child instanceof NarratableEntry narratable) {
                this.narratables.add(narratable);
            }
            if (child instanceof GuiEventListener listener) {
                this.listeners.add(listener);
            }
        });
        this.lines.add(line);
    }

    public void refreshState() {
        for (AbstractLine line : this.lines) {
            line.refreshState();
        }
    }

    @Override
    public void visitChildren(Consumer<LayoutElement> consumer) {
        this.vertical.visitChildren(consumer);
    }

    public void arrangeElements() {
        for (AbstractLine line : this.lines) {
            int leftEdgeWidth = line.header ? LEFT_EDGE_WIDTH_HEADER : LEFT_EDGE_WIDTH_SUB;
            line.labelWidget.setWidth(this.getContentWidth() - leftEdgeWidth - INPUT_WIDTH - RIGHT_EDGE_WIDTH);
        }
        this.vertical.setPosition(this.getContentX(), this.getContentY());
        this.vertical.arrangeElements();
    }

    @Override
    public void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
        this.arrangeElements();

        if (this.lines.size() > 1) {
            // Draw horizontal lines on all non-headers
            for (AbstractLine line : this.lines) {
                if (!line.header) {
                    StringWidget widget = line.labelWidget;
                    int x1 = this.getContentX() + 6;
                    int x2 = x1 + 4;
                    int y = widget.getY() + 8;
                    guiGraphics.hLine(x1, x2, y, TREE_COLOR);
                }
            }

            // Draw vertical line
            StringWidget topWidget = this.lines.getFirst().labelWidget;
            StringWidget bottomWidget = this.lines.getLast().labelWidget;
            int x = this.getContentX() + 6;
            int y1 = topWidget.getBottom() - 1;
            int y2 = bottomWidget.getY() + 8;
            guiGraphics.vLine(x, y1, y2, TREE_COLOR);
        }

        if (hovered) {
            for (AbstractLine line : this.lines) {
                int x1 = this.getContentX();
                int y1 = line.horizontal.getY();
                int x2 = this.getContentRight();
                int y2 = y1 + line.horizontal.getHeight();
                if (guiGraphics.containsPointInScissor(mouseX, mouseY) && mouseX >= x1 && mouseY >= y1 && mouseX < x2 && mouseY < y2) {
                    // Draw hover background
                    guiGraphics.fill(x1, y1, x2, y2, HOVER_COLOR);
                }
            }
        }

        for (Renderable renderable : this.renderables) {
            renderable.render(guiGraphics, mouseX, mouseY, deltaTicks);
        }
    }

    @Override
    public int getContentHeight() {
        return this.vertical.getHeight();
    }

    @Override
    public int getHeight() {
        return this.getContentHeight() + 4;
    }

    @Override
    public @NotNull List<? extends NarratableEntry> narratables() {
        return this.narratables;
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return this.listeners;
    }
}
