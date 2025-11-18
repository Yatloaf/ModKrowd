package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.world.entity.EntityEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
	@Inject(method = "handlePlayerInfoRemove", at = @At("RETURN"))
	public void handlePlayerInfoRemoveInject(ClientboundPlayerInfoRemovePacket packet, CallbackInfo ci) {
		ModKrowd.invalidateTabListCache();
	}

	@Inject(method = "handlePlayerInfoUpdate", at = @At("RETURN"))
	public void handlePlayerInfoUpdateInject(ClientboundPlayerInfoUpdatePacket packet, CallbackInfo ci) {
		ModKrowd.invalidateTabListCache();
	}

	@Inject(method = "handleEntityEvent", at = @At("RETURN"))
	public void handleEntityEventInject(ClientboundEntityEventPacket packet, CallbackInfo ci) {
		byte status = packet.getEventId();
		if (status >= EntityEvent.PERMISSION_LEVEL_ALL && status <= EntityEvent.PERMISSION_LEVEL_OWNERS) {
			ModKrowd.CONFIG.updateFeatures();
		}
	}
}