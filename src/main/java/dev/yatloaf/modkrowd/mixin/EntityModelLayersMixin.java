package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.util.Util;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EquipmentModelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityModelLayers.class)
public class EntityModelLayersMixin {

    // TODO: Find better solution to Yarn mess-up workaround
    @Inject(method = "registerEquipment", at = @At("RETURN"))
    private static void registerEquipmentInject(String id, CallbackInfoReturnable<EquipmentModelData<EntityModelLayer>> cir) {
        if ("player_slim".equals(id)) {
            Util.PLAYER_SLIM_EQUIPMENT = cir.getReturnValue();
        }
    }
}
