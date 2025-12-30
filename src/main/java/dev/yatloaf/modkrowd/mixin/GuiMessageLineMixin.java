package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.mixinduck.GuiMessageLineDuck;
import net.minecraft.client.GuiMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GuiMessage.Line.class)
public class GuiMessageLineMixin implements GuiMessageLineDuck {
    @Unique
    private int backgroundTint = 0;

    @Override
    public void modKrowd$setBackgroundTint(int color) {
        this.backgroundTint = color;
    }

    @Override
    public int modKrowd$getBackgroundTint() {
        return this.backgroundTint;
    }
}
