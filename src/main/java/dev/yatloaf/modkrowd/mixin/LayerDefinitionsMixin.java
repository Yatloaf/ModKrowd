package dev.yatloaf.modkrowd.mixin;

import com.google.common.collect.ImmutableMap;
import dev.yatloaf.modkrowd.config.Features;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.ArmorModelSet;
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
@Mixin(LayerDefinitions.class)
public class LayerDefinitionsMixin {
	// SLIM_ARMOR

    @Shadow @Final private static CubeDeformation OUTER_ARMOR_DEFORMATION;

    // Ordinals would be brittle, this replaces every call but is only called on reload anyway
    // Even better would be reassigning the keys at the end, but then build() would have to be replaced by buildKeepingLast()
    @Redirect(method = "createRoots", at = @At(value = "INVOKE", remap = false, target = "Lnet/minecraft/client/renderer/entity/ArmorModelSet;putFrom(Lnet/minecraft/client/renderer/entity/ArmorModelSet;Lcom/google/common/collect/ImmutableMap$Builder;)V"))
    private static <T> void putFromRedirect(ArmorModelSet<T> instance, ArmorModelSet<LayerDefinition> texturedModelData, ImmutableMap.Builder<T, LayerDefinition> builder) {
        if (Features.SLIM_ARMOR.active && instance == ModelLayers.PLAYER_SLIM_ARMOR) {
            // .map() would modify every part, we only want to modify the chest
            ArmorModelSet<LayerDefinition> slimData = new ArmorModelSet<>(
                    texturedModelData.head(),
                    texturedModelData.chest().apply(LayerDefinitionsMixin::transformSlim),
                    texturedModelData.legs(),
                    texturedModelData.feet()
            );
            instance.putFrom(slimData, builder);
        } else {
            instance.putFrom(texturedModelData, builder);
        }
    }

    // Instance of functional interface MeshTransformer
    // See BabyModelTransform for an example
    @Unique
    private static MeshDefinition transformSlim(MeshDefinition source) {
        // Make a copy to avoid mutating the non-slim version
        // Only the first layer has to be copied, since everything further nested is kept the same

        MeshDefinition result = new MeshDefinition();
        PartDefinition resultRoot = result.getRoot();
        PartDefinition sourceRoot = source.getRoot();

        for (Map.Entry<String, PartDefinition> entry : sourceRoot.getChildren()) {
            String key = entry.getKey();
            PartDefinition value = entry.getValue();
            resultRoot.addOrReplaceChild(key, value);

            // Double addChild call because it deals with the mess of transferring children to the new object
            // Only do this if the parts actually exist
            switch (key) {
                case PartNames.RIGHT_ARM -> resultRoot.addOrReplaceChild(
                        PartNames.RIGHT_ARM,
                        CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, OUTER_ARMOR_DEFORMATION),
                        PartPose.offset(-5.0F, 2.0F, 0.0F)
                );
                case PartNames.LEFT_ARM -> resultRoot.addOrReplaceChild(
                        PartNames.LEFT_ARM,
                        CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, OUTER_ARMOR_DEFORMATION),
                        PartPose.offset(5.0F, 2.0F, 0.0F)
                );
            }
        }

        return result;
    }

}