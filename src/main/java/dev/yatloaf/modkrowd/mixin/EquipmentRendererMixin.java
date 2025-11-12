package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.client.render.entity.equipment.EquipmentRenderer;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.item.equipment.trim.ArmorTrim;
import net.minecraft.item.equipment.trim.ArmorTrimPattern;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(EquipmentRenderer.class)
public class EquipmentRendererMixin {
    // SLIM_ARMOR

    // Modify pattern to slim version, revert asset to normal
    // Reader is encouraged to come up with a more elegant solution
    @ModifyArgs(method = "render(Lnet/minecraft/client/render/entity/equipment/EquipmentModel$LayerType;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/util/Identifier;II)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/equipment/EquipmentRenderer$TrimSpriteKey;<init>(Lnet/minecraft/item/equipment/trim/ArmorTrim;Lnet/minecraft/client/render/entity/equipment/EquipmentModel$LayerType;Lnet/minecraft/registry/RegistryKey;)V"))
    private void TrimSpriteKeyArgs(Args args) {
        if (!ModKrowd.CONFIG.SLIM_ARMOR.enabled) return;

        RegistryKey<EquipmentAsset> assetKey = args.get(2);
        Identifier assetKeyId = assetKey.getValue();
        String assetKeyPath = assetKeyId.getPath();
        if (!assetKeyPath.endsWith("_slim")) return;

        RegistryKey<EquipmentAsset> normalAssetKey = RegistryKey.of(
                EquipmentAssetKeys.REGISTRY_KEY,
                assetKeyId.withPath(assetKeyPath.replace("_slim", ""))
        );
        args.set(2, normalAssetKey);

        ArmorTrim armorTrim = args.get(0);
        ArmorTrimPattern pattern = armorTrim.pattern().value();
        ArmorTrimPattern slimPattern = new ArmorTrimPattern(pattern.assetId().withSuffixedPath("_slim"), pattern.description(), pattern.decal());
        ArmorTrim slimArmorTrim = new ArmorTrim(armorTrim.material(), RegistryEntry.of(slimPattern));
        args.set(0, slimArmorTrim);
    }
}
