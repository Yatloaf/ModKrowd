package dev.yatloaf.modkrowd.mixinduck;

import dev.yatloaf.modkrowd.cubekrowd.message.MessageCache;
import org.jspecify.annotations.Nullable;

public interface GuiMessageDuck {
    void modKrowd$setMessageCache(MessageCache cache);
    @Nullable MessageCache modKrowd$getMessageCache();
}
