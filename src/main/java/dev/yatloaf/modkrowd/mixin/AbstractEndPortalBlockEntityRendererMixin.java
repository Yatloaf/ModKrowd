package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.block.entity.EndPortalBlockEntity;
import net.minecraft.client.render.block.entity.AbstractEndPortalBlockEntityRenderer;
import net.minecraft.client.render.block.entity.state.EndPortalBlockEntityRenderState;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractEndPortalBlockEntityRenderer.class)
public class AbstractEndPortalBlockEntityRendererMixin<T extends EndPortalBlockEntity, S extends EndPortalBlockEntityRenderState> {
	// TANGIBLE_END_PORTALS

	// Replaced with normal model
	// Overridden method isn't cancelled, so the gateway beam still renders
	@Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/render/block/entity/state/EndPortalBlockEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/render/state/CameraRenderState;)V", cancellable = true)
	private void renderInject(S endPortalBlockEntityRenderState, MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, CameraRenderState cameraRenderState, CallbackInfo ci) {
		if (ModKrowd.CONFIG.TANGIBLE_END_PORTALS.enabled) {
			ci.cancel();
		}
	}
}
