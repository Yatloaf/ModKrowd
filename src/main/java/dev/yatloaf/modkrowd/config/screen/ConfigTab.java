package dev.yatloaf.modkrowd.config.screen;

import dev.yatloaf.modkrowd.config.FeatureTab;
import dev.yatloaf.modkrowd.config.FeatureState;
import dev.yatloaf.modkrowd.config.feature.Feature;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ConfigTab implements Tab {
    public final FeatureTab tab;
    public final FeatureListWidget listWidget;

    public ConfigTab(ConfigScreen screen, FeatureTab tab) {
        this.tab = tab;
        // width and height are set in refreshGrid(...)
        this.listWidget = new FeatureListWidget(Minecraft.getInstance(), screen, 0, 0, 0, 20);
    }

    @Override
    public @NotNull Component getTabTitle() {
        return this.tab.name;
    }

    @Override
    public @NotNull Component getTabExtraNarration() {
        // Accessibility? This is the default implementation in GridScreenTab
        return Component.empty();
    }

    @Override
    public void visitChildren(Consumer<AbstractWidget> consumer) {
        this.listWidget.visitWidgets(consumer);
    }

    @Override
    public void doLayout(ScreenRectangle tabArea) {
        // Unlike setDimensionsAndPosition, position also calls recalculateAllChildrenPositions
        this.listWidget.updateSizeAndPosition(tabArea.width(), tabArea.height(), 0, tabArea.top());
        this.listWidget.arrangeElements();
    }

    public void refreshState() {
        this.listWidget.refreshState();
    }

    public class FeatureListWidget extends ContainerObjectSelectionList<FeatureEntry> {
        public FeatureListWidget(Minecraft minecraft, ConfigScreen screen, int width, int height, int y, int itemHeight) {
            super(minecraft, width, height, y, itemHeight);
            for (Feature feature : ConfigTab.this.tab.features) {
                FeatureState featureState = screen.config.getState(feature);
                FeatureEntry entry = new FeatureEntry(screen, featureState);
                entry.arrangeElements();
                this.addEntry(entry, entry.getHeight());
            }
        }

        public void refreshState() {
            this.children().forEach(FeatureEntry::refreshState);
        }

        public void arrangeElements() {
            this.children().forEach(FeatureEntry::arrangeElements);
        }

        @Override
        public int getRowWidth() {
            return 384;
        }
    }
}
