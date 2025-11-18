package dev.yatloaf.modkrowd.mixin;

import com.mojang.authlib.GameProfile;
import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.RemotePlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RemotePlayer.class)
public class RemotePlayerMixin extends AbstractClientPlayer {
	// HIDE_PLAYERS

	public RemotePlayerMixin(ClientLevel world, GameProfile profile) {
		super(world, profile);
	}

	// Unlike isInvisible[To], this also controls armor rendering, perfect for hiding players
	@Inject(at = @At("HEAD"), method = "shouldRenderAtSqrDistance", cancellable = true)
	private void shouldRenderAtSqrDistanceInject(double distance, CallbackInfoReturnable<Boolean> cir) {
		if (ModKrowd.CONFIG.HIDE_PLAYERS.enabled) {
			cir.setReturnValue(false);
		}
	}
}
