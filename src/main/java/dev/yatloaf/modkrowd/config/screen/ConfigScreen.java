package dev.yatloaf.modkrowd.config.screen;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.config.Config;
import dev.yatloaf.modkrowd.config.SyncedConfig;
import dev.yatloaf.modkrowd.config.exception.ConfigException;
import dev.yatloaf.modkrowd.util.Util;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

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
    private final TabNavigationBar tabNavigation;

    public final Button reloadButton;
    public final Button cancelButton;
    public final Button doneButton;

    public ConfigScreen(Screen parent, SyncedConfig config, Save save, Cancel cancel) {
        super(Component.translatable("modkrowd.config.title"));
        this.parent = parent;
        this.config = config.copyConfig(); // Local unsynced copy
        this.save = save;
        this.cancel = cancel;
        this.tabs = Util.listToArray(this.config.tabs, ConfigTab::new, ConfigTab[]::new);
        this.tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);
        this.tabNavigation = TabNavigationBar.builder(this.tabManager, this.width).addTabs(this.tabs).build();
        this.reloadButton = Button.builder(Component.literal("Reload"), this::onReloadButton).build();
        this.cancelButton = Button.builder(Component.literal("Cancel"), this::onCancelButton).build();
        this.doneButton = Button.builder(Component.literal("Done"), this::onDoneButton).build();
    }

    private void onReloadButton(Button button) {
        try {
            this.config.deserialize(ModKrowd.CONFIG_FILE);
            for (ConfigTab t : this.tabs) {
                t.refreshState();
            }
        } catch (ConfigException e) {
            ModKrowd.LOGGER.error("[ConfigScreen] Couldn't load config!", e);
        }
    }

    private void onCancelButton(Button button) {
        this.cancelAndClose();
    }

    private void onDoneButton(Button button) {
        this.saveAndClose();
    }

    private void cancelAndClose() {
        this.updateConfig();
        this.cancel.cancel(this.config);
        this.onClose();
    }

    private void saveAndClose() {
        this.updateConfig();
        this.save.save(this.config);
        this.onClose();
    }

    private void updateConfig() {
        ConfigTab currentTab = (ConfigTab) this.tabManager.getCurrentTab();
        assert currentTab != null;
        this.config.selectedTab = currentTab.tab;
    }

    @Override
    protected void init() {
        this.tabNavigation.setWidth(this.width);
        this.tabNavigation.arrangeElements(); // Enhanced by TabNavigationBarMixin <3

        int contentTop = this.tabNavigation.getRectangle().bottom();
        ScreenRectangle tabArea = new ScreenRectangle(0, contentTop, this.width, this.height - contentTop - 36);
        this.tabManager.setTabArea(tabArea);

        this.reloadButton.setRectangle(64, 20, this.width - 216, this.height - 28);
        this.cancelButton.setRectangle(64, 20, this.width - 144, this.height - 28);
        this.doneButton.setRectangle(64, 20, this.width - 72, this.height - 28);

        this.addRenderableWidget(this.tabNavigation);

        Tab currentTab = this.tabManager.getCurrentTab();
        if (currentTab == null) {
            this.tabNavigation.selectTab(this.config.selectedTab.index, false);
        } else {
            currentTab.visitChildren(this::addRenderableWidget); // This is already done in tabManager.selectTab()
        }

        this.addRenderableWidget(this.reloadButton);
        this.addRenderableWidget(this.cancelButton);
        this.addRenderableWidget(this.doneButton);
    }

    @Override
    public boolean keyPressed(KeyEvent input) {
        // Save even when pressing Esc
        if (input.isEscape()) {
            this.saveAndClose();
            return true;
        } else {
            return super.keyPressed(input);
        }
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
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
