package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.config.Features;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
    // HIDE_SELF

    // Hide first person arm
    @Redirect(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;isInvisible()Z"))
    private boolean isInvisibleRedirect(AbstractClientPlayer instance) {
        return instance.isInvisible() || Features.HIDE_SELF.active;
    }
}
