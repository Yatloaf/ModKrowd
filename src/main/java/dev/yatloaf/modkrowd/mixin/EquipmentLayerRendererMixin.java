package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(EquipmentLayerRenderer.class)
public class EquipmentLayerRendererMixin {
    // SLIM_ARMOR

    // Modify pattern to slim version, revert asset to normal
    // Reader is encouraged to come up with a more elegant solution
    @ModifyArgs(method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/ResourceLocation;II)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer$TrimSpriteKey;<init>(Lnet/minecraft/world/item/equipment/trim/ArmorTrim;Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;)V"))
    private void TrimSpriteKeyArgs(Args args) {
        if (!ModKrowd.CONFIG.SLIM_ARMOR.enabled) return;

        ResourceKey<EquipmentAsset> assetKey = args.get(2);
        ResourceLocation assetKeyId = assetKey.location();
        String assetKeyPath = assetKeyId.getPath();
        if (!assetKeyPath.endsWith("_slim")) return;

        ResourceKey<EquipmentAsset> normalAssetKey = ResourceKey.create(
                EquipmentAssets.ROOT_ID,
                assetKeyId.withPath(assetKeyPath.replace("_slim", ""))
        );
        args.set(2, normalAssetKey);

        ArmorTrim armorTrim = args.get(0);
        TrimPattern pattern = armorTrim.pattern().value();
        TrimPattern slimPattern = new TrimPattern(pattern.assetId().withSuffix("_slim"), pattern.description(), pattern.decal());
        ArmorTrim slimArmorTrim = new ArmorTrim(armorTrim.material(), Holder.direct(slimPattern));
        args.set(0, slimArmorTrim);
    }
}
