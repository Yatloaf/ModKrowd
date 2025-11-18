package dev.yatloaf.modkrowd.mixin;

import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(KeyMapping.class)
public interface KeyMappingAccessor {
    // Why is this method not public?

    @Invoker
    void callRelease();
}
