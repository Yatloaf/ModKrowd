package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.config.Features;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity> {
	// DINNERBONE_GRUMM
	// DEADMAU5

	// Ignore the cape requirement
	// This only gets called for player entities, so no instanceof required
	// Don't set the flipUpsideDown field directly because that is handled by a superclass, so it would affect all entities
	@Inject(method = "isEntityUpsideDown(Lnet/minecraft/world/entity/Avatar;)Z", at = @At("HEAD"), cancellable = true)
	private void isEntityUpsideDownInject(AvatarlikeEntity avatar, CallbackInfoReturnable<Boolean> cir) {
		if (Features.DINNERBONE_GRUMM.active) {
			cir.setReturnValue(true);
		}
	}

	// Set the extraEars field directly instead of redirecting it every time
	@Redirect(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/ClientAvatarEntity;showExtraEars()Z"))
	private boolean showExtraEarsRedirect(ClientAvatarEntity instance) {
		return Features.DEADMAU5.active || instance.showExtraEars();
	}
}