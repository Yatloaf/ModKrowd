package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
	// UNINVISIBILITY

	// See invisible entities
	@Inject(at = @At("HEAD"), method = "isInvisibleTo", cancellable = true)
	private void isInvisibleToInject(Player player, CallbackInfoReturnable<Boolean> cir) {
		if (ModKrowd.CONFIG.UNINVISIBILITY.enabled) {
			cir.setReturnValue(false);
		}
	}
}
