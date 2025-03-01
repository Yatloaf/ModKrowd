package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    // RESPECTATE

    @Shadow @Final private MinecraftClient client;

    // If the camera entity is removed, start spectating the entity with the same network ID again
    @Inject(method = "tick", at = @At("HEAD"))
    public void tickInject(CallbackInfo ci) {
        if (ModKrowd.CONFIG.RESPECTATE.enabled) {
            if (this.client.world != null && this.client.cameraEntity != null && this.client.cameraEntity.isRemoved()) {
                Entity respawned = this.client.world.getEntityById(this.client.cameraEntity.getId());
                if (respawned != null) {
                    this.client.setCameraEntity(respawned);
                }
            }
        }
    }
}
