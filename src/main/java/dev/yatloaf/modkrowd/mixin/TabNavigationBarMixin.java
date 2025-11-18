package dev.yatloaf.modkrowd.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TabNavigationBar.class)
public class TabNavigationBarMixin {
    // Slightly improve the config screen

    @Shadow @Final private ImmutableList<Tab> tabs;

    @Redirect(method = "arrangeElements", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I"))
    private int minRedirect(int a, int b) {
        return Math.min(100 + 100 * this.tabs.size(), b);
    }
}
