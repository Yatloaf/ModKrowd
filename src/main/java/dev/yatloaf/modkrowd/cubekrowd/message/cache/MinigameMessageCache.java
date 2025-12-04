package dev.yatloaf.modkrowd.cubekrowd.message.cache;

import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.Message;
import dev.yatloaf.modkrowd.cubekrowd.message.MinigameChatMessage;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public class MinigameMessageCache extends CubeKrowdMessageCache { // NOT a superclass of FishslapMessageCache
    public final Subserver subserver;

    private MinigameChatMessage minigameChatMessage;

    public MinigameMessageCache(TextCache original, Subserver subserver) {
        super(original);
        this.subserver = subserver;
    }

    @Override
    protected Message createResult() {
        MinigameChatMessage minigameChatMessage = this.minigameChatMessageFast();
        if (minigameChatMessage.isReal()) {
            return minigameChatMessage;
        }

        return super.createResult();
    }

    public MinigameChatMessage minigameChatMessageFast() {
        if (this.minigameChatMessage == null) {
            this.minigameChatMessage = MinigameChatMessage.readFast(StyledStringReader.of(this.original.styledString()), this.subserver);
        }
        return this.minigameChatMessage;
    }
}
