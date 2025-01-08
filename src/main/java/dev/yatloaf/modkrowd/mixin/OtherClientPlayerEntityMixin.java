package dev.yatloaf.modkrowd.mixin;

import com.mojang.authlib.GameProfile;
import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OtherClientPlayerEntity.class)
public class OtherClientPlayerEntityMixin extends AbstractClientPlayerEntity {
	// HIDE_PLAYERS

	public OtherClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}

	// Unlike isInvisible[To], this also controls armor rendering, perfect for hiding players
	@Inject(at = @At("HEAD"), method = "shouldRender", cancellable = true)
	private void shouldRenderInject(double distance, CallbackInfoReturnable<Boolean> cir) {
		if (ModKrowd.CONFIG.HIDE_PLAYERS.enabled) {
			cir.setReturnValue(false);
		}
	}
}
