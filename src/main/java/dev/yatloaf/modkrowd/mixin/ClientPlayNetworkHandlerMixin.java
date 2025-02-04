package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRemoveS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
	@Inject(method = "onPlayerRemove", at = @At("RETURN"))
	public void onPlayerRemoveInject(PlayerRemoveS2CPacket packet, CallbackInfo ci) {
		ModKrowd.invalidateTabListCache();
	}

	@Inject(method = "onPlayerList", at = @At("RETURN"))
	public void onPlayerListInject(PlayerListS2CPacket packet, CallbackInfo ci) {
		ModKrowd.invalidateTabListCache();
	}
}