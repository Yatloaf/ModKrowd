package dev.yatloaf.modkrowd.mixin;

import dev.yatloaf.modkrowd.cubekrowd.message.cache.MessageCache;
import dev.yatloaf.modkrowd.mixinduck.GuiMessageLineDuck;
import net.minecraft.client.GuiMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GuiMessage.Line.class)
public class GuiMessageLineMixin implements GuiMessageLineDuck {
    @Unique
    private MessageCache cache = null;

    @Override
    public void modKrowd$setMessageCache(MessageCache cache) {
        this.cache = cache;
    }

    @Override
    public MessageCache modKrowd$getMessageCache() {
        return this.cache;
    }
}
