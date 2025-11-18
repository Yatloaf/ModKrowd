package dev.yatloaf.modkrowd.mixin;

import com.mojang.authlib.GameProfile;
import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin extends AbstractClientPlayer {
    // HIDE_SELF

    public LocalPlayerMixin(ClientLevel world, GameProfile profile) {
        super(world, profile);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return super.shouldRenderAtSqrDistance(distance) && !ModKrowd.CONFIG.HIDE_SELF.enabled;
    }
}
