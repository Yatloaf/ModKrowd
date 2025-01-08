package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> {
	// DINNERBONE_GRUMM
	// SHOW_OWN_LABEL
	// ALWAYS_SHOW_LABELS
	// ALWAYS_HIDE_LABELS

	// Turn everything upside down for absolutely no reason
	@Inject(at = @At("HEAD"), method = "shouldFlipUpsideDown", cancellable = true)
	private static void shouldFlipUpsideDownInject(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		if (ModKrowd.CONFIG.DINNERBONE_GRUMM.enabled && entity instanceof PlayerEntity) {
			cir.setReturnValue(true);
		}
	}

	// Hud appears always disabled
	// This overrides the other mixins below
	@Inject(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
	private void hasLabelInject(T livingEntity, CallbackInfoReturnable<Boolean> cir) {
		if (ModKrowd.CONFIG.ALWAYS_HIDE_LABELS.enabled) {
			cir.setReturnValue(false);
		}
	}

	// Sabotage attempts to figure out if the entity in question happens to be the player
	@Redirect(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getCameraEntity()Lnet/minecraft/entity/Entity;"))
	private Entity getCameraEntityRedirect(MinecraftClient instance) {
        return ModKrowd.CONFIG.SHOW_OWN_LABEL.enabled ? null : instance.getCameraEntity(); // Only for ==, no danger of NullPointerException
	}

	// Hud appears always enabled
	@Redirect(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;isHudEnabled()Z"))
	private boolean isHudEnabledRedirect() {
        return ModKrowd.CONFIG.ALWAYS_SHOW_LABELS.enabled || MinecraftClient.isHudEnabled();
	}
}
