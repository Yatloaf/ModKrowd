package dev.yatloaf.modkrowd.config.screen;

import dev.yatloaf.modkrowd.config.FeatureTab;
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

    public ConfigTab(FeatureTab tab) {
        this.tab = tab;
        // width and height are set in refreshGrid(...)
        this.listWidget = new FeatureListWidget(Minecraft.getInstance(), 0, 0, 0, 20);
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
    }

    public void refreshState() {
        this.listWidget.refreshState();
    }

    public class FeatureListWidget extends ContainerObjectSelectionList<AbstractEntry> {
        public FeatureListWidget(Minecraft minecraft, int width, int height, int y, int itemHeight) {
            super(minecraft, width, height, y, itemHeight);
            for (Feature feature : ConfigTab.this.tab.features) {
                for (AbstractEntry entry : feature.createScreenEntries(minecraft)) {
                    this.addEntry(entry);
                }
            }
        }

        public void refreshState() {
            this.children().forEach(AbstractEntry::refreshState);
        }

        @Override
        public int getRowWidth() {
            return 256;
        }
    }
}
