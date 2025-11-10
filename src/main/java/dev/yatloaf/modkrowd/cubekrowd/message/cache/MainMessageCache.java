package dev.yatloaf.modkrowd.cubekrowd.message.cache;

import dev.yatloaf.modkrowd.cubekrowd.message.AlohaMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.FishslapChatMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.MainChatMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.Message;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public class MainMessageCache extends CubeKrowdMessageCache {
    private MainChatMessage mainChatMessage;
    private FishslapChatMessage fishslapChatMessage;

    public MainMessageCache(TextCache original) {
        super(original);
    }

    @Override
    protected Message createResult() {
        AlohaMessage alohaMessage = this.alohaMessageFast();
        if (alohaMessage.isReal()) {
            return alohaMessage;
        }

        MainChatMessage mainChatMessage = this.mainChatMessageFast();
        if (mainChatMessage.isReal()) {
            return mainChatMessage;
        }

        // Fishslap-style messages in BuildTeam
        FishslapChatMessage fishslapChatMessage = this.fishslapChatMessageFast();
        if (fishslapChatMessage.isReal()) {
            return fishslapChatMessage;
        }

        return super.createResult();
    }

    public final MainChatMessage mainChatMessageFast() {
        if (this.mainChatMessage == null) {
            this.mainChatMessage = MainChatMessage.readFast(StyledStringReader.of(this.original.styledString()));
        }
        return this.mainChatMessage;
    }

    public final FishslapChatMessage fishslapChatMessageFast() {
        if (this.fishslapChatMessage == null) {
            this.fishslapChatMessage = FishslapChatMessage.readFast(StyledStringReader.of(this.original.styledString()));
        }
        return this.fishslapChatMessage;
    }
}
