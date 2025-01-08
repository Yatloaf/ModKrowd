package dev.yatloaf.modkrowd.mixin;

import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(KeyBinding.class)
public interface KeyBindingAccessor {
    // Why is this method not public?

    @Invoker
    void callReset();
}
