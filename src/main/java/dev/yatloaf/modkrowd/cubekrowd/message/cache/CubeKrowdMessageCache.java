package dev.yatloaf.modkrowd.cubekrowd.message.cache;

import dev.yatloaf.modkrowd.cubekrowd.message.AfkMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.DirectMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.KickedMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.Message;
import dev.yatloaf.modkrowd.cubekrowd.message.UnavailableMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.WhereamiMessage;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public class CubeKrowdMessageCache extends MessageCache {
    private WhereamiMessage whereamiMessage;
    private AfkMessage afkMessage;
    private DirectMessage directMessage;
    private UnavailableMessage unavailableMessage;
    private KickedMessage kickedMessage;

    public CubeKrowdMessageCache(TextCache original) {
        super(original);
    }

    @Override
    protected Message createResult() {
        WhereamiMessage whereamiMessage = this.whereamiMessageFast();
        if (whereamiMessage.isReal()) {
            return whereamiMessage;
        }

        AfkMessage afkMessage = this.afkMessageFast();
        if (afkMessage.isReal()) {
            return afkMessage;
        }

        DirectMessage directMessage = this.directMessageFast();
        if (directMessage.isReal()) {
            return directMessage;
        }

        UnavailableMessage unavailableMessage = this.unavailableMessageFast();
        if (unavailableMessage.isReal()) {
            return unavailableMessage;
        }

        KickedMessage kickedMessage = this.kickedMessageFast();
        if (kickedMessage.isReal()) {
            return kickedMessage;
        }

        return Message.FAILURE;
    }

    public final WhereamiMessage whereamiMessageFast() {
        if (this.whereamiMessage == null) {
            this.whereamiMessage = WhereamiMessage.readFast(StyledStringReader.of(this.original.styledString()));
        }
        return this.whereamiMessage;
    }

    public final AfkMessage afkMessageFast() {
        if (this.afkMessage == null) {
            this.afkMessage = AfkMessage.readFast(StyledStringReader.of(this.original.styledString()));
        }
        return this.afkMessage;
    }

    public final DirectMessage directMessageFast() {
        if (this.directMessage == null) {
            this.directMessage = DirectMessage.readFast(StyledStringReader.of(this.original.styledString()));
        }
        return this.directMessage;
    }

    public final UnavailableMessage unavailableMessageFast() {
        if (this.unavailableMessage == null) {
            this.unavailableMessage = UnavailableMessage.readFast(StyledStringReader.of(this.original.styledString()));
        }
        return this.unavailableMessage;
    }

    public final KickedMessage kickedMessageFast() {
        if (this.kickedMessage == null) {
            this.kickedMessage = KickedMessage.readFast(StyledStringReader.of(this.original.styledString()));
        }
        return this.kickedMessage;
    }
}
