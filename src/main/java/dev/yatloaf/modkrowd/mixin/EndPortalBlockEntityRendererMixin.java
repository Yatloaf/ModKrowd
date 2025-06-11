package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.block.entity.EndPortalBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndPortalBlockEntityRenderer.class)
public class EndPortalBlockEntityRendererMixin<T extends EndPortalBlockEntity> {
	// TANGIBLE_END_PORTALS

	// Replaced with normal model
	@Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/block/entity/EndPortalBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/util/math/Vec3d;)V", cancellable = true)
	private void renderInject(T endPortalBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, Vec3d vec3d, CallbackInfo ci) {
		if (ModKrowd.CONFIG.TANGIBLE_END_PORTALS.enabled) {
			ci.cancel();
		}
	}
}
