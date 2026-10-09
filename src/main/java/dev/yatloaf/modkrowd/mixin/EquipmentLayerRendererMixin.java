package dev.yatloaf.modkrowd.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.yatloaf.modkrowd.config.Features;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.HashSet;
import java.util.Set;

@Mixin(EquipmentLayerRenderer.class)
public class EquipmentLayerRendererMixin {
    // SLIM_ARMOR

    @Unique
    private static final Set<Identifier> SLIMMABLE = new HashSet<>();
    static {
        SLIMMABLE.add(EquipmentAssets.LEATHER.identifier());
        SLIMMABLE.add(EquipmentAssets.COPPER.identifier());
        SLIMMABLE.add(EquipmentAssets.CHAINMAIL.identifier());
        SLIMMABLE.add(EquipmentAssets.IRON.identifier());
        SLIMMABLE.add(EquipmentAssets.GOLD.identifier());
        SLIMMABLE.add(EquipmentAssets.DIAMOND.identifier());
        SLIMMABLE.add(EquipmentAssets.NETHERITE.identifier());
    }

    // Modify armor and trim to slim version
    // For some reason specifying the target and local names doesn't work, but in this case the types suffice
    @SuppressWarnings({"ModifyVariableMayUseName", "LocalMayUseName"})
    @ModifyVariable(method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V",
            at = @At("HEAD"), argsOnly = true)
    private <S> ResourceKey<@NonNull EquipmentAsset> modifyEquipmentAssetId(
            ResourceKey<@NonNull EquipmentAsset> equipmentAssetId,
            @Local(argsOnly = true) EquipmentClientInfo.LayerType layerType,
            @Local(argsOnly = true) S state
    ) {
        Identifier id = equipmentAssetId.identifier();

        if (Features.SLIM_ARMOR.active
                // Don't include HUMANOID_LEGGINGS! And HUMANOID_BABY I guess, but that will never happen anyway
                && layerType == EquipmentClientInfo.LayerType.HUMANOID
                && state instanceof AvatarRenderState avatarRenderState
                && avatarRenderState.skin.model() == PlayerModelType.SLIM
                && SLIMMABLE.contains(id)) {
            return ResourceKey.create(equipmentAssetId.registryKey(), id.withSuffix("_slim"));
        } else {
            return equipmentAssetId;
        }
    }
}
