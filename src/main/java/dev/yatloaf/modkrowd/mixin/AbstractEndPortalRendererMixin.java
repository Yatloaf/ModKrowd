package dev.yatloaf.modkrowd.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.yatloaf.modkrowd.config.Features;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.function.Consumer;

@Mixin(AbstractEndPortalRenderer.class)
public class AbstractEndPortalRendererMixin {
	// TANGIBLE_END_PORTALS

	// Replaced with normal model
	// Overridden method isn't cancelled, so the gateway beam still renders
	@Inject(at = @At("HEAD"), method = "submitCube(Ljava/util/Collection;Lnet/minecraft/client/renderer/rendertype/RenderType;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Ljava/util/function/Consumer;)V", cancellable = true)
	private static void submitCubeInject(Collection<Direction> facesToShow, RenderType renderType, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, Consumer<VertexConsumer> vertexDecorator, CallbackInfo ci) {
		if (Features.TANGIBLE_END_PORTALS.active) {
			ci.cancel();
		}
	}
}
