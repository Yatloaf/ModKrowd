package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.client.network.ClientPlayerLikeEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.PlayerLikeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin<AvatarlikeEntity extends PlayerLikeEntity & ClientPlayerLikeEntity> {
	// DINNERBONE_GRUMM
	// DEADMAU5

	// Ignore the cape requirement
	// This only gets called for player entities, so no instanceof required
	// Don't set the flipUpsideDown field directly because that is handled by a superclass, so it would affect all entities
	@Inject(method = "shouldFlipUpsideDown(Lnet/minecraft/entity/PlayerLikeEntity;)Z", at = @At("HEAD"), cancellable = true)
	private void shouldFlipUpsideDownInject(AvatarlikeEntity playerLikeEntity, CallbackInfoReturnable<Boolean> cir) {
		if (ModKrowd.CONFIG.DINNERBONE_GRUMM.enabled) {
			cir.setReturnValue(true);
		}
	}

	// Set the extraEars field directly instead of redirecting it every time
	@Redirect(method = "updateRenderState(Lnet/minecraft/entity/PlayerLikeEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerLikeEntity;hasExtraEars()Z"))
	private boolean hasExtraEarsRedirect(ClientPlayerLikeEntity instance) {
		return ModKrowd.CONFIG.DEADMAU5.enabled || instance.hasExtraEars();
	}
}