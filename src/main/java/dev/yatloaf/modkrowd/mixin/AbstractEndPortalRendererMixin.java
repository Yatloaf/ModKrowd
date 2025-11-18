package dev.yatloaf.modkrowd.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.minecraft.client.renderer.blockentity.state.EndPortalRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractEndPortalRenderer.class)
public class AbstractEndPortalRendererMixin<T extends TheEndPortalBlockEntity, S extends EndPortalRenderState> {
	// TANGIBLE_END_PORTALS

	// Replaced with normal model
	// Overridden method isn't cancelled, so the gateway beam still renders
	@Inject(at = @At("HEAD"), method = "submit(Lnet/minecraft/client/renderer/blockentity/state/EndPortalRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", cancellable = true)
	private void submitInject(S endPortalBlockEntityRenderState, PoseStack matrixStack, SubmitNodeCollector orderedRenderCommandQueue, CameraRenderState cameraRenderState, CallbackInfo ci) {
		if (ModKrowd.CONFIG.TANGIBLE_END_PORTALS.enabled) {
			ci.cancel();
		}
	}
}
