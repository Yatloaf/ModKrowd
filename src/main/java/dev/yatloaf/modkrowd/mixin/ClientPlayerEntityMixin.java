package dev.yatloaf.modkrowd.mixin;

import com.mojang.authlib.GameProfile;
import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    // HIDE_SELF

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Override
    public boolean shouldRender(double distance) {
        return super.shouldRender(distance) && !ModKrowd.CONFIG.HIDE_SELF.enabled;
    }
}
