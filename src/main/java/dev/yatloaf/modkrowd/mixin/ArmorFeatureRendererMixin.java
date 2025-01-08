package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.mixinduck.ArmorMaterialLayerDuck;
import dev.yatloaf.modkrowd.mixinduck.ArmorTrimDuck;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> {
	// SLIM_ARMOR

	@Unique
	private boolean slim = false;

	// Save whether it's slim
	@Inject(method = "renderArmor", at = @At("HEAD"))
	private void renderArmorInject(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light, A model, CallbackInfo ci) {
		this.slim = ModKrowd.CONFIG.SLIM_ARMOR.enabled
				&& entity instanceof AbstractClientPlayerEntity player
				&& player.getSkinTextures().model() == SkinTextures.Model.SLIM;
	}

	// Return slim armor texture instead
	@Redirect(method = "renderArmor", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ArmorMaterial$Layer;getTexture(Z)Lnet/minecraft/util/Identifier;"))
	private Identifier getTextureRedirect(ArmorMaterial.Layer instance, boolean secondLayer) {
		return this.slim
				? ((ArmorMaterialLayerDuck)(Object) instance).modKrowd$getSlimTexture(secondLayer)
				: instance.getTexture(secondLayer);
	}

	// Leggings are the same regardless of slim

	// Return slim trim texture instead
	@Redirect(method = "renderTrim", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/trim/ArmorTrim;getGenericModelId(Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/util/Identifier;"))
	private Identifier getGenericModelIdRedirect(ArmorTrim instance, RegistryEntry<ArmorMaterial> armorMaterial) {
        return this.slim
				? ((ArmorTrimDuck) instance).modKrowd$getGenericSlimModelId(armorMaterial)
				: instance.getGenericModelId(armorMaterial);
    }
}
