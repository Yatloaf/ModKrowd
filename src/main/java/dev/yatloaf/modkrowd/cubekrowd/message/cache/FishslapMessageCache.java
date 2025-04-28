package dev.yatloaf.modkrowd.cubekrowd.message.cache;

import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.FishslapChatMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.Message;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public class FishslapMessageCache extends CubeKrowdMessageCache {
    private FishslapChatMessage fishslapChatMessage;

    public FishslapMessageCache(TextCache original) {
        super(original);
    }

    @Override
    protected Message createResult() {
        FishslapChatMessage fishslapChatMessage = this.fishslapChatMessageFast();
        if (fishslapChatMessage.isReal()) {
            return fishslapChatMessage;
        }

        return super.createResult();
    }

    public FishslapChatMessage fishslapChatMessageFast() {
        if (this.fishslapChatMessage == null) {
            this.fishslapChatMessage = FishslapChatMessage.readFast(StyledStringReader.of(this.original.styledString()));
        }
        return this.fishslapChatMessage;
    }
}
