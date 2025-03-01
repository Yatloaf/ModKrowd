package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.client.render.entity.feature.Deadmau5FeatureRenderer;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Deadmau5FeatureRenderer.class)
public class Deadmau5FeatureRendererMixin {
	// DEADMAU5

	@Unique
	private static final Identifier DEADMAU5_SKIN = Identifier.of("modkrowd", "textures/entity/player/wide/deadmau5.png");

	// Always render the ears
	@Redirect(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/PlayerEntityRenderState;FF)V", at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z"))
	private boolean equalsRedirect(String string, Object object) {
		return ModKrowd.CONFIG.DEADMAU5.enabled || string.equals(object);
	}

	// Return deadmau5' skin instead
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/SkinTextures;texture()Lnet/minecraft/util/Identifier;"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/PlayerEntityRenderState;FF)V")
	private Identifier textureRedirect(SkinTextures instance) {
		return ModKrowd.CONFIG.DEADMAU5.enabled ? DEADMAU5_SKIN : instance.texture();
	}
}