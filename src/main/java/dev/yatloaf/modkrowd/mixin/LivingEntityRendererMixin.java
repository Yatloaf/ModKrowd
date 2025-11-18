package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> {
	// SHOW_OWN_LABEL
	// ALWAYS_SHOW_LABELS
	// ALWAYS_HIDE_LABELS

	// Hud appears always disabled
	// This overrides the other mixins below
	@Inject(method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;D)Z", at = @At("HEAD"), cancellable = true)
	private void shouldShowNameInject(T livingEntity, double d, CallbackInfoReturnable<Boolean> cir) {
		if (ModKrowd.CONFIG.ALWAYS_HIDE_LABELS.enabled) {
			cir.setReturnValue(false);
		}
	}

	// Sabotage attempts to figure out if the entity in question happens to be the player
	@Redirect(method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;D)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getCameraEntity()Lnet/minecraft/world/entity/Entity;"))
	private Entity getCameraEntityRedirect(Minecraft instance) {
        return ModKrowd.CONFIG.SHOW_OWN_LABEL.enabled ? null : instance.getCameraEntity(); // Only for ==, no danger of NullPointerException
	}

	// Hud appears always enabled
	@Redirect(method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;D)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;renderNames()Z"))
	private boolean renderNamesRedirect() {
        return ModKrowd.CONFIG.ALWAYS_SHOW_LABELS.enabled || Minecraft.renderNames();
	}
}
