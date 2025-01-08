package dev.yatloaf.modkrowd.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.widget.TabNavigationWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TabNavigationWidget.class)
public class TabNavigationWidgetMixin {
    // Slightly improve the config screen

    @Shadow @Final private ImmutableList<Tab> tabs;

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I"))
    private int minRedirect(int a, int b) {
        return Math.min(100 + 100 * this.tabs.size(), b);
    }
}
