package dev.yatloaf.modkrowd.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerSkinType;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<S extends BipedEntityRenderState, M extends BipedEntityModel<S>, A extends BipedEntityModel<S>> {
	// SLIM_ARMOR

    @Unique
    private static final Set<Identifier> SLIMMABLE = new HashSet<>();
    static {
        SLIMMABLE.add(EquipmentAssetKeys.LEATHER.getValue());
        SLIMMABLE.add(EquipmentAssetKeys.COPPER.getValue());
        SLIMMABLE.add(EquipmentAssetKeys.CHAINMAIL.getValue());
        SLIMMABLE.add(EquipmentAssetKeys.IRON.getValue());
        SLIMMABLE.add(EquipmentAssetKeys.GOLD.getValue());
        SLIMMABLE.add(EquipmentAssetKeys.DIAMOND.getValue());
        SLIMMABLE.add(EquipmentAssetKeys.NETHERITE.getValue());
    }

	@Unique
	private boolean slim = false;

	// Save whether it's slim
	@Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V", at = @At("HEAD"))
	private void renderInject(MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, int i, S bipedEntityRenderState, float f, float g, CallbackInfo ci) {
		this.slim = ModKrowd.CONFIG.SLIM_ARMOR.enabled
				&& bipedEntityRenderState instanceof PlayerEntityRenderState playerEntityRenderState
				&& playerEntityRenderState.skinTextures.model() == PlayerSkinType.SLIM;
	}

	// Modify asset to slim version
	// See also EquipmentRendererMixin
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Redirect(method = "renderArmor", at = @At(value = "INVOKE", target = "Ljava/util/Optional;orElseThrow()Ljava/lang/Object;"))
	private Object orElseThrowRedirect(Optional<RegistryKey<EquipmentAsset>> instance, @Local(argsOnly = true) EquipmentSlot slot) {
		RegistryKey<EquipmentAsset> result = instance.orElseThrow();
        Identifier id = result.getValue();
		if (!this.slim || slot != EquipmentSlot.CHEST || !SLIMMABLE.contains(id)) return result;

		return RegistryKey.of(EquipmentAssetKeys.REGISTRY_KEY, id.withSuffixedPath("_slim"));
	}
}
