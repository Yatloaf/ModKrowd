package dev.yatloaf.modkrowd.mixinduck;

import net.minecraft.item.ArmorMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public interface ArmorTrimDuck {
    Identifier modKrowd$getGenericSlimModelId(RegistryEntry<ArmorMaterial> armorMaterial);
}
