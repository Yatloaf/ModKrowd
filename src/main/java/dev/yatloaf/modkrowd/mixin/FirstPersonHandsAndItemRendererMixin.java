package dev.yatloaf.modkrowd.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.yatloaf.modkrowd.config.Features;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public class FirstPersonHandsAndItemRendererMixin {
    // HIDE_SELF

    // Hide first person arm
    @Inject(method = "submitHandsWithItems", at = @At("HEAD"))
    private void submitHandsWithItemsInject(float partialTicks, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, PlayerRenderState playerState, FirstPersonHandsAndItemsRenderState state, CallbackInfo ci) {
        if (Features.HIDE_SELF.active && playerState.avatarRenderState != null) {
            // Hopefully this will be reset by the next frame
            playerState.avatarRenderState.isInvisible = true;
        }
    }
}
