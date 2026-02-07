package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.config.Features;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.LevelHeightAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.ClientLevelData.class)
public class ClientLevelDataMixin {
	// DEVOID

	// Dark horizon only below the world
	@Inject(at = @At("HEAD"), method = "getHorizonHeight", cancellable = true)
	private void getHorizonHeightInject(LevelHeightAccessor world, CallbackInfoReturnable<Double> cir) {
		if (Features.DEVOID.active) {
			cir.setReturnValue((double) world.getMinY());
		}
	}
}
