package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.client.renderer.entity.layers.Deadmau5EarsLayer;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Deadmau5EarsLayer.class)
public class Deadmau5EarsLayerMixin {
	// DEADMAU5

	@Unique
	private static final ResourceLocation DEADMAU5_SKIN = ResourceLocation.fromNamespaceAndPath("modkrowd", "textures/entity/player/wide/deadmau5.png");

	// Return deadmau5' skin instead
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/core/ClientAsset$Texture;texturePath()Lnet/minecraft/resources/ResourceLocation;"), method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/AvatarRenderState;FF)V")
	private ResourceLocation texturePathRedirect(ClientAsset.Texture instance) {
		return ModKrowd.CONFIG.DEADMAU5.enabled ? DEADMAU5_SKIN : instance.texturePath();
	}
}