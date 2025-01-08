package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.mixinduck.ArmorMaterialLayerDuck;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorMaterial.Layer.class)
public class ArmorMaterialLayerMixin implements ArmorMaterialLayerDuck {
    // SLIM_ARMOR

    // Add slim variants in a totally-not-convoluted way

    // Normally, only layer_1 variants would require slim variants, but resource packs might add more on layer_2
    // Additionally, if a resource pack is enabled, mixing the pack's layer_2 with this mod's layer_1 would look horrible

    @Shadow @Final private Identifier id;
    @Shadow @Final private String suffix;

    @Unique
    private /* final */ Identifier layer2TextureSlim;
    @Unique
    private /* final */ Identifier layer1TextureSlim;

    @Inject(method = "<init>(Lnet/minecraft/util/Identifier;Ljava/lang/String;Z)V", at = @At("RETURN"))
    private void LayerInject(Identifier id, String suffix, boolean dyeable, CallbackInfo ci) {
        this.layer2TextureSlim = this.getSlimTextureId(true);
        this.layer1TextureSlim = this.getSlimTextureId(false);
    }

    @Unique
    private Identifier getSlimTextureId(boolean secondLayer) {
        return this.id.withPath(
                path -> "textures/models/armor/" + path + "_layer_" + (secondLayer ? "2" : "1") + "_slim" + this.suffix + ".png"
        );
    }

    @Override
    public Identifier modKrowd$getSlimTexture(boolean secondLayer) {
        return secondLayer ? this.layer2TextureSlim : this.layer1TextureSlim;
    }
}
