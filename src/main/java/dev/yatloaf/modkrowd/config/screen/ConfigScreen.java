package dev.yatloaf.modkrowd.config.screen;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.config.Config;
import dev.yatloaf.modkrowd.config.SyncedConfig;
import dev.yatloaf.modkrowd.config.exception.ConfigException;
import dev.yatloaf.modkrowd.util.Util;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TabNavigationWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;

/**
 * May your deity of choice help you
 */
public class ConfigScreen extends Screen {
    public final Screen parent;
    public final Config config;
    public final Save save;
    public final Cancel cancel;
    public final ConfigTab[] tabs;

    private final TabManager tabManager;
    private final TabNavigationWidget tabNavigation;

    public final ButtonWidget reloadButton;
    public final ButtonWidget cancelButton;
    public final ButtonWidget doneButton;

    public ConfigScreen(Screen parent, SyncedConfig config, Save save, Cancel cancel) {
        super(Text.translatable("modkrowd.config.title"));
        this.parent = parent;
        this.config = config.copyConfig(); // Local unsynced copy
        this.save = save;
        this.cancel = cancel;
        this.tabs = Util.listToArray(this.config.tabs, ConfigTab::new, ConfigTab[]::new);
        this.tabManager = new TabManager(this::addDrawableChild, this::remove);
        this.tabNavigation = TabNavigationWidget.builder(this.tabManager, this.width).tabs(this.tabs).build();
        this.reloadButton = ButtonWidget.builder(Text.literal("Reload"), this::onReloadButton).build();
        this.cancelButton = ButtonWidget.builder(Text.literal("Cancel"), this::onCancelButton).build();
        this.doneButton = ButtonWidget.builder(Text.literal("Done"), this::onDoneButton).build();
    }

    private void onReloadButton(ButtonWidget button) {
        try {
            this.config.deserialize(ModKrowd.CONFIG_FILE);
            for (ConfigTab t : this.tabs) {
                t.refreshState();
            }
        } catch (ConfigException e) {
            ModKrowd.LOGGER.error("[ConfigScreen] Couldn't load config!", e);
        }
    }

    private void onCancelButton(ButtonWidget button) {
        this.cancelAndClose();
    }

    private void onDoneButton(ButtonWidget button) {
        this.saveAndClose();
    }

    private void cancelAndClose() {
        this.updateConfig();
        this.cancel.cancel(this.config);
        this.close();
    }

    private void saveAndClose() {
        this.updateConfig();
        this.save.save(this.config);
        this.close();
    }

    private void updateConfig() {
        ConfigTab currentTab = (ConfigTab) this.tabManager.getCurrentTab();
        assert currentTab != null;
        this.config.selectedTab = currentTab.tab;
    }

    @Override
    protected void init() {
        this.tabNavigation.setWidth(this.width);
        this.tabNavigation.init(); // Enhanced by TabNavigationWidgetMixin <3

        int contentTop = this.tabNavigation.getNavigationFocus().getBottom();
        ScreenRect tabArea = new ScreenRect(0, contentTop, this.width, this.height - contentTop - 36);
        this.tabManager.setTabArea(tabArea);

        this.reloadButton.setDimensionsAndPosition(64, 20, this.width - 216, this.height - 28);
        this.cancelButton.setDimensionsAndPosition(64, 20, this.width - 144, this.height - 28);
        this.doneButton.setDimensionsAndPosition(64, 20, this.width - 72, this.height - 28);

        this.addDrawableChild(this.tabNavigation);

        Tab currentTab = this.tabManager.getCurrentTab();
        if (currentTab == null) {
            this.tabNavigation.selectTab(this.config.selectedTab.index, false);
        } else {
            currentTab.forEachChild(this::addDrawableChild); // This is already done in tabManager.selectTab()
        }

        this.addDrawableChild(this.reloadButton);
        this.addDrawableChild(this.cancelButton);
        this.addDrawableChild(this.doneButton);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        // Save even when pressing Esc
        if (input.isEscape()) {
            this.saveAndClose();
            return true;
        } else {
            return super.keyPressed(input);
        }
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }

    @FunctionalInterface
    public interface Save {
        void save(Config config);
    }
    @FunctionalInterface
    public interface Cancel {
        void cancel(Config config);
    }
}
