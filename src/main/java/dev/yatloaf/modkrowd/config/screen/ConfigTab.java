package dev.yatloaf.modkrowd.config.screen;

import dev.yatloaf.modkrowd.config.FeatureTab;
import dev.yatloaf.modkrowd.config.feature.Feature;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class ConfigTab implements Tab {
    public final FeatureTab tab;
    public final FeatureListWidget listWidget;

    public ConfigTab(FeatureTab tab) {
        this.tab = tab;
        // width and height are set in refreshGrid(...)
        this.listWidget = new FeatureListWidget(MinecraftClient.getInstance(), 0, 0, 0, 20);
    }

    @Override
    public Text getTitle() {
        return this.tab.name;
    }

    @Override
    public Text getNarratedHint() {
        // Accessibility? This is the default implementation in GridScreenTab
        return Text.empty();
    }

    @Override
    public void forEachChild(Consumer<ClickableWidget> consumer) {
        this.listWidget.forEachChild(consumer);
    }

    @Override
    public void refreshGrid(ScreenRect tabArea) {
        // Unlike setDimensionsAndPosition, position also calls recalculateAllChildrenPositions
        this.listWidget.position(tabArea.width(), tabArea.height(), 0, tabArea.getTop());
    }

    public void refreshState() {
        this.listWidget.refreshState();
    }

    public class FeatureListWidget extends ElementListWidget<AbstractEntry> {
        public FeatureListWidget(MinecraftClient client, int width, int height, int y, int itemHeight) {
            super(client, width, height, y, itemHeight);
            for (Feature feature : ConfigTab.this.tab.features) {
                for (AbstractEntry entry : feature.createScreenEntries(client)) {
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
