package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.cubekrowd.common.RankName;
import dev.yatloaf.modkrowd.cubekrowd.common.SelfPlayer;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.world.entity.EntityEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(method = "handleTabListCustomisation", at = @At("RETURN"))
    public void handleTabListCustomisationInject(ClientboundTabListPacket clientboundTabListPacket, CallbackInfo ci) {
        ModKrowd.TAB_DECO.invalidateAll();
    }

	@Inject(method = "handlePlayerInfoRemove", at = @At("RETURN"))
	public void handlePlayerInfoRemoveInject(ClientboundPlayerInfoRemovePacket packet, CallbackInfo ci) {
		ModKrowd.TAB_LIST.invalidateAll();
	}

	@Inject(method = "handlePlayerInfoUpdate", at = @At("RETURN"))
	public void handlePlayerInfoUpdateInject(ClientboundPlayerInfoUpdatePacket packet, CallbackInfo ci) {
		ModKrowd.TAB_LIST.invalidateAll();

        // Try to initialize rankName with a non-FAILURE value as early as possible
        // Displaying an outdated rank after a promotion is forgivable, but <[?] > is not
        if (ModKrowd.currentSubserver.isCubeKrowd && SelfPlayer.rankName == RankName.FAILURE) {
            SelfPlayer.rankNameSoft();
        }
	}

	@Inject(method = "handleEntityEvent", at = @At("RETURN"))
	public void handleEntityEventInject(ClientboundEntityEventPacket packet, CallbackInfo ci) {
		byte status = packet.getEventId();
		if (status >= EntityEvent.PERMISSION_LEVEL_ALL && status <= EntityEvent.PERMISSION_LEVEL_OWNERS) {
			ModKrowd.CONFIG.updateFeatures();
		}
	}
}