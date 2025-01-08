package dev.yatloaf.modkrowd.cubekrowd.message.cache;

import dev.yatloaf.modkrowd.cubekrowd.message.AlohaMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.MainChatMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.Message;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;
import net.minecraft.text.TranslatableTextContent;

public class MainMessageCache extends CubeKrowdMessageCache {
    private AlohaMessage alohaMessage;
    private MainChatMessage mainChatMessage;

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

        return super.createResult();
    }

    public final AlohaMessage alohaMessageFast() {
        if (this.alohaMessage == null) {
            if (this.original.text().getContent() instanceof TranslatableTextContent content) {
                this.alohaMessage = AlohaMessage.parseFast(content);
            } else {
                this.alohaMessage = AlohaMessage.FAILED;
            }
        }
        return this.alohaMessage;
    }

    public final MainChatMessage mainChatMessageFast() {
        if (this.mainChatMessage == null) {
            this.mainChatMessage = MainChatMessage.readFast(StyledStringReader.of(this.original.styledString()));
        }
        return this.mainChatMessage;
    }
}
