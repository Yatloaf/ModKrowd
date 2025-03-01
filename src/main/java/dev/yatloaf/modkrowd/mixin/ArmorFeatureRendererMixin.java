package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<S extends BipedEntityRenderState, M extends BipedEntityModel<S>, A extends BipedEntityModel<S>> {
	// SLIM_ARMOR

	@Unique
	private boolean slim = false;

	// Save whether it's slim
	@Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V", at = @At("HEAD"))
	private void renderInject(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, S bipedEntityRenderState, float f, float g, CallbackInfo ci) {
		this.slim = ModKrowd.CONFIG.SLIM_ARMOR.enabled
				&& bipedEntityRenderState instanceof PlayerEntityRenderState playerEntityRenderState
				&& playerEntityRenderState.skinTextures.model() == SkinTextures.Model.SLIM;
	}

	// Modify asset to slim version
	// See also EquipmentRendererMixin
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Redirect(method = "renderArmor", at = @At(value = "INVOKE", target = "Ljava/util/Optional;orElseThrow()Ljava/lang/Object;"))
	private Object orElseThrowRedirect(Optional<RegistryKey<EquipmentAsset>> instance) {
		RegistryKey<EquipmentAsset> result = instance.orElseThrow();
		if (!this.slim) return result;

		return RegistryKey.of(EquipmentAssetKeys.REGISTRY_KEY, result.getValue().withSuffixedPath("_slim"));
	}
}
