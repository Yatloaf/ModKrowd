package dev.yatloaf.modkrowd.cubekrowd.message.cache;

import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.AlohaMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.Message;

/**
 * Used for messages most likely to occur while the subserver is PENDING
 */
public class FakeMessageCache extends CubeKrowdMessageCache {
    public FakeMessageCache(TextCache original) {
        super(original);
    }

    @Override
    protected Message createResult() {
        AlohaMessage alohaMessage = this.alohaMessageFast();
        if (alohaMessage.isReal()) {
            return alohaMessage;
        }

        return super.createResult();
    }
}
