package dev.yatloaf.modkrowd.mixin;

import com.google.common.collect.ImmutableMap;
import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.util.Util;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.EntityModels;
import net.minecraft.client.render.entity.model.EquipmentModelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

/**
 * No longer includes a fix for <a href="https://bugs.mojang.com/browse/MC-100929">MC-100929</a>,
 * which is even more noticeable with slim armor, due to this being a Sisyphean task
 */
@Mixin(EntityModels.class)
public class EntityModelsMixin {
	// SLIM_ARMOR

    @Shadow @Final private static Dilation ARMOR_DILATION;

    // YARN!!! Why are there two fields named PLAYER_SLIM?!
    // Ordinals would be brittle, this replaces every call but is only called on reload anyway
    // Even better would be reassigning the keys at the end, but then build() would have to be replaced by buildKeepingLast()
    @Redirect(method = "getModels", at = @At(value = "INVOKE", remap = false, target = "Lnet/minecraft/client/render/entity/model/EquipmentModelData;addTo(Lnet/minecraft/client/render/entity/model/EquipmentModelData;Lcom/google/common/collect/ImmutableMap$Builder;)V"))
    private static <T> void addToRedirect(EquipmentModelData<T> instance, EquipmentModelData<TexturedModelData> texturedModelData, ImmutableMap.Builder<T, TexturedModelData> builder) {
        if (ModKrowd.CONFIG.SLIM_ARMOR.enabled && instance == Util.PLAYER_SLIM_EQUIPMENT) {
            EquipmentModelData<TexturedModelData> data3 = texturedModelData.map(data -> data.transform(EntityModelsMixin::transformSlim));
            instance.addTo(data3, builder);
        } else {
            instance.addTo(texturedModelData, builder);
        }
    }

    // Instance of functional interface ModelTransformer
    // See BabyModelTransformer for an example
    @Unique
    private static ModelData transformSlim(ModelData source) {
        // Make a copy to avoid mutating the non-slim version
        // Only the first layer has to be copied, since everything further nested is kept the same

        ModelData result = new ModelData();
        ModelPartData resultRoot = result.getRoot();
        ModelPartData sourceRoot = source.getRoot();

        for (Map.Entry<String, ModelPartData> entry : sourceRoot.getChildren()) {
            resultRoot.addChild(entry.getKey(), entry.getValue());
        }

        // A double addChild call is better than a condition inside the loop: It does overwrite the value we just copied
        // in, but also deals with the mess of transferring children to the new object

        resultRoot.addChild(
                EntityModelPartNames.RIGHT_ARM,
                ModelPartBuilder.create().uv(40, 16).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, ARMOR_DILATION),
                ModelTransform.origin(-5.0F, 2.0F, 0.0F)
        );
        resultRoot.addChild(
                EntityModelPartNames.LEFT_ARM,
                ModelPartBuilder.create().uv(40, 16).mirrored().cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, ARMOR_DILATION),
                ModelTransform.origin(5.0F, 2.0F, 0.0F)
        );

        return result;
    }

}