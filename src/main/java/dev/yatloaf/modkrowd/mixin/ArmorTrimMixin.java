package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.mixinduck.ArmorTrimDuck;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterial;
import net.minecraft.item.trim.ArmorTrimPattern;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Function;

@Mixin(ArmorTrim.class)
public class ArmorTrimMixin implements ArmorTrimDuck {
    // SLIM_ARMOR

    @Shadow @Final private RegistryEntry<ArmorTrimMaterial> material;
    @Shadow @Final private RegistryEntry<ArmorTrimPattern> pattern;

    @Shadow
    private static String getMaterialAssetNameFor(RegistryEntry<ArmorTrimMaterial> material, RegistryEntry<ArmorMaterial> armorMaterial) {
        return null;
    }

    // snout_gold -> snout_slim_gold
    // NOT snout_leggings_gold -> snout_slim_leggings_gold because only arms are affected
    @Unique
    private /* final */ Function<RegistryEntry<ArmorMaterial>, Identifier> genericSlimModelIdGetter;

    @Override
    public Identifier modKrowd$getGenericSlimModelId(RegistryEntry<ArmorMaterial> armorMaterial) {
        if (this.genericSlimModelIdGetter == null) {
            this.genericSlimModelIdGetter = Util.memoize(materialEntry -> {
                Identifier identifier = this.pattern.value().assetId();
                String string = getMaterialAssetNameFor(this.material, materialEntry);
                return identifier.withPath(materialName -> "trims/models/armor/" + materialName + "_slim_" + string);
            });
        }
        return this.genericSlimModelIdGetter.apply(armorMaterial);
    }
}
