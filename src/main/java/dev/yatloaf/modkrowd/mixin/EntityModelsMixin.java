package dev.yatloaf.modkrowd.mixin;

import com.google.common.collect.ImmutableMap;
import dev.yatloaf.modkrowd.ModKrowd;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.ArmorEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.EntityModels;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * No longer includes a fix for <a href="https://bugs.mojang.com/browse/MC-100929">MC-100929</a>,
 * which is even more noticeable with slim armor, due to this being a Sisyphean task
 */
@Mixin(EntityModels.class)
public class EntityModelsMixin {
	// SLIM_ARMOR

    @Shadow @Final private static Dilation HAT_DILATION;
    @Shadow @Final private static Dilation ARMOR_DILATION;

    // Type erasure issues...
    // Ordinals would be brittle, this replaces every call but is only called on reload anyway
    // Even better would be reassigning the keys at the end, but then build() would have to be replaced by buildKeepingLast()
    @Redirect(method = "getModels", at = @At(value = "INVOKE", remap = false, target = "Lcom/google/common/collect/ImmutableMap$Builder;put(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;"))
    private static ImmutableMap.Builder<EntityModelLayer, TexturedModelData> putRedirect(ImmutableMap.Builder<EntityModelLayer, TexturedModelData> instance, Object key, Object value) {
        EntityModelLayer layer = (EntityModelLayer) key;
        if (ModKrowd.CONFIG.SLIM_ARMOR.enabled) {
            if (layer == EntityModelLayers.PLAYER_SLIM_INNER_ARMOR) {
                return instance.put(layer, TexturedModelData.of(getModelData(HAT_DILATION), 64, 32));
            }
            if (layer == EntityModelLayers.PLAYER_SLIM_OUTER_ARMOR) {
                return instance.put(layer, TexturedModelData.of(getModelData(ARMOR_DILATION), 64, 32));
            }
        }
        return instance.put(layer, (TexturedModelData) value);
    }

    // See ArmorEntityModel.getModelData()
    @Unique
    private static ModelData getModelData(Dilation dilation) {
        ModelData modelData = ArmorEntityModel.getModelData(dilation, 0.0F);
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(
                EntityModelPartNames.RIGHT_ARM,
                ModelPartBuilder.create().uv(40, 16).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation),
                ModelTransform.pivot(-5.0F, 2.0F, 0.0F)
        );
        modelPartData.addChild(
                EntityModelPartNames.LEFT_ARM,
                ModelPartBuilder.create().uv(40, 16).mirrored().cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation),
                ModelTransform.pivot(5.0F, 2.0F, 0.0F)
        );
        return modelData;
    }
}