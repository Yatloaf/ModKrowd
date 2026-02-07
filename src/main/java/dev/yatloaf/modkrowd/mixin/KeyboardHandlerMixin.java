package dev.yatloaf.modkrowd.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.config.FeatureState;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {
    // In addition to KeyMapping#click, toggle every feature whose key bind is set to this one
    @Inject(method = "keyPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;click(Lcom/mojang/blaze3d/platform/InputConstants$Key;)V"))
    private void keyPressInject(long l, int i, KeyEvent keyEvent, CallbackInfo ci) {
        InputConstants.Key key = InputConstants.getKey(keyEvent);
        for (FeatureState featureState : ModKrowd.CONFIG.states()) {
            if (featureState.toggleKey == key) {
                featureState.enabled = !featureState.enabled;
            }
        }
        ModKrowd.CONFIG.updateFeatures();
    }
}
