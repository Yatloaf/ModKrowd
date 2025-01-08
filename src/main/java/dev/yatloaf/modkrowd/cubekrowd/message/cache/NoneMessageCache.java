package dev.yatloaf.modkrowd.cubekrowd.message.cache;

import dev.yatloaf.modkrowd.cubekrowd.message.Message;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;

public class NoneMessageCache extends MessageCache {
    public NoneMessageCache(TextCache original) {
        super(original);
    }

    @Override
    protected Message createResult() {
        return Message.FAILURE;
    }
}
