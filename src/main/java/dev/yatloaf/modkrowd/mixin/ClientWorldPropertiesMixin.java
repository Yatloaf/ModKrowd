package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.HeightLimitView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.Properties.class)
public class ClientWorldPropertiesMixin {
	// DEVOID

	// Dark horizon only below the world
	@Inject(at = @At("HEAD"), method = "getSkyDarknessHeight", cancellable = true)
	private void getSkyDarknessHeightInject(HeightLimitView world, CallbackInfoReturnable<Double> cir) {
		if (ModKrowd.CONFIG.DEVOID.enabled) {
			cir.setReturnValue((double) world.getBottomY());
		}
	}
}
