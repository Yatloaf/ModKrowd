package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    // RESPECTATE

    @Shadow @Final private Minecraft minecraft;

    // If the camera entity is removed, start spectating the entity with the same network ID again
    @Inject(method = "tick", at = @At("HEAD"))
    public void tickInject(CallbackInfo ci) {
        if (ModKrowd.CONFIG.RESPECTATE.enabled) {
            Entity cameraEntity = this.minecraft.getCameraEntity();
            if (this.minecraft.level != null && cameraEntity != null && cameraEntity.isRemoved()) {
                Entity respawned = this.minecraft.level.getEntity(cameraEntity.getId());
                if (respawned != null) {
                    this.minecraft.setCameraEntity(respawned);
                }
            }
        }
    }
}
