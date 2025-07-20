package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.mixinduck.ChatHudLineDuck;
import net.minecraft.client.gui.hud.ChatHudLine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ChatHudLine.class)
public class ChatHudLineMixin implements ChatHudLineDuck {
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

    @Mixin(ChatHudLine.Visible.class)
    public static class ChatHudLineVisibleMixin implements ChatHudLineDuck {
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
}
