package dev.yatloaf.modkrowd.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.components.TabButton;
import net.minecraft.client.gui.components.tabs.MenuTabBar;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MenuTabBar.class)
public class MenuTabBarMixin extends TabNavigationBar {
    // Slightly improve the config screen

    protected MenuTabBarMixin(int x, int y, int width, int height, TabManager tabManager, ImmutableList<@NotNull TabButton> tabButtons, ImmutableList<@NotNull Tab> tabs) {
        super(x, y, width, height, tabManager, tabButtons, tabs);
    }

    @Redirect(method = "arrangeElements", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I"))
    private int minRedirect(int a, int b) {
        return Math.min(100 + 100 * this.tabs.size(), b);
    }
}
