package dev.yatloaf.modkrowd.cubekrowd.message.cache;

import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.Message;

public class NoneMessageCache extends MessageCache {
    public NoneMessageCache(TextCache original) {
        super(original);
    }

    @Override
    protected Message createResult() {
        return Message.FAILURE;
    }
}
