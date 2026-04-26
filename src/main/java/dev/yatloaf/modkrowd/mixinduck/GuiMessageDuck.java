package dev.yatloaf.modkrowd.mixinduck;

import dev.yatloaf.modkrowd.cubekrowd.message.MessageCache;

public interface GuiMessageDuck {
    void modKrowd$setMessageCache(MessageCache cache);
    MessageCache modKrowd$getMessageCache();
}
