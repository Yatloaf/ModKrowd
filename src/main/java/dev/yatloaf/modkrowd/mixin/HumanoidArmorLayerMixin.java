package dev.yatloaf.modkrowd.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.yatloaf.modkrowd.config.Features;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<S extends HumanoidRenderState> {
	// SLIM_ARMOR

    @Unique
    private static final Set<ResourceLocation> SLIMMABLE = new HashSet<>();
    static {
        SLIMMABLE.add(EquipmentAssets.LEATHER.location());
        SLIMMABLE.add(EquipmentAssets.COPPER.location());
        SLIMMABLE.add(EquipmentAssets.CHAINMAIL.location());
        SLIMMABLE.add(EquipmentAssets.IRON.location());
        SLIMMABLE.add(EquipmentAssets.GOLD.location());
        SLIMMABLE.add(EquipmentAssets.DIAMOND.location());
        SLIMMABLE.add(EquipmentAssets.NETHERITE.location());
    }

	@Unique
	private boolean slim = false;

	// Save whether it's slim
	@Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V", at = @At("HEAD"))
	private void submitInject(PoseStack matrixStack, SubmitNodeCollector orderedRenderCommandQueue, int i, S bipedEntityRenderState, float f, float g, CallbackInfo ci) {
		this.slim = Features.SLIM_ARMOR.active
				&& bipedEntityRenderState instanceof AvatarRenderState playerEntityRenderState
				&& playerEntityRenderState.skin.model() == PlayerModelType.SLIM;
	}

	// Modify asset to slim version
	// See also EquipmentLayerRendererMixin
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Redirect(method = "renderArmorPiece", at = @At(value = "INVOKE", target = "Ljava/util/Optional;orElseThrow()Ljava/lang/Object;"))
	private Object orElseThrowRedirect(Optional<ResourceKey<EquipmentAsset>> instance, @Local(argsOnly = true) EquipmentSlot slot) {
		ResourceKey<EquipmentAsset> result = instance.orElseThrow();
        ResourceLocation id = result.location();
		if (!this.slim || slot != EquipmentSlot.CHEST || !SLIMMABLE.contains(id)) return result;

		return ResourceKey.create(EquipmentAssets.ROOT_ID, id.withSuffix("_slim"));
	}
}
