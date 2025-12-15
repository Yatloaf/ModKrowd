package dev.yatloaf.modkrowd.cubekrowd.message.cache;

import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.ThemedCache;
import dev.yatloaf.modkrowd.cubekrowd.message.AfkMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.AlohaMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.ConnectingMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.DirectMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.MixedChatMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.KickedMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.RankChatMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.Message;
import dev.yatloaf.modkrowd.cubekrowd.message.TeamChatMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.DeathMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.MissileWarsGameEndMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.UnavailableMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.WhereamiMessage;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public class MessageCache extends ThemedCache {
    public final Subserver subserver;

    private boolean blocked = false;
    private int backgroundTint = 0;
    private Message result;

    public MessageCache(TextCache original, Subserver subserver) {
        super(original);
        this.subserver = subserver;
    }

    public static MessageCache of(TextCache message, Subserver subserver) {
        return new MessageCache(message, subserver);
    }

    public boolean blocked() {
        return this.blocked;
    }

    public void setBlocked(boolean value) {
        this.blocked = value;
    }

    public int backgroundTint() {
        return this.backgroundTint;
    }

    public void setBackgroundTint(int color) {
        this.backgroundTint = color & 0x00FFFFFF;
    }

    public final Message result() {
        if (this.result == null) {
            this.result = this.createResult();
        }
        return this.result;
    }

    private Message createResult() {
        AlohaMessage alohaMessage = AlohaMessage.parseFast(this.original.text().getContents());
        if (alohaMessage.isReal()) return alohaMessage;

        DeathMessage deathMessage = DeathMessage.parseFast(this.original.text().getContents());
        if (deathMessage.isReal()) return deathMessage;

        if (this.subserver.isCubeKrowd) {
            // Should be roughly sorted by frequency

            RankChatMessage rankChatMessage = RankChatMessage.readFast(StyledStringReader.of(this.original.styledString()));
            if (rankChatMessage.isReal()) return rankChatMessage;

            MixedChatMessage mixedChatMessage = MixedChatMessage.readFast(StyledStringReader.of(this.original.styledString()));
            if (mixedChatMessage.isReal()) return mixedChatMessage;

            TeamChatMessage teamChatMessage = TeamChatMessage.readFast(StyledStringReader.of(this.original.styledString()), this.subserver);
            if (teamChatMessage.isReal()) return teamChatMessage;

            AfkMessage afkMessage = AfkMessage.readFast(StyledStringReader.of(this.original.styledString()));
            if (afkMessage.isReal()) return afkMessage;

            DirectMessage directMessage = DirectMessage.readFast(StyledStringReader.of(this.original.styledString()));
            if (directMessage.isReal()) return directMessage;

            WhereamiMessage whereamiMessage = WhereamiMessage.readFast(StyledStringReader.of(this.original.styledString()));
            if (whereamiMessage.isReal()) return whereamiMessage;

            ConnectingMessage connectingMessage = ConnectingMessage.readFast(StyledStringReader.of(this.original.styledString()));
            if (connectingMessage.isReal()) return connectingMessage;

            MissileWarsGameEndMessage missileWarsGameEndMessage = MissileWarsGameEndMessage.readFast(StyledStringReader.of(this.original.styledString()));
            if (missileWarsGameEndMessage.isReal()) return missileWarsGameEndMessage;

            UnavailableMessage unavailableMessage = UnavailableMessage.readFast(StyledStringReader.of(this.original.styledString()));
            if (unavailableMessage.isReal()) return unavailableMessage;

            KickedMessage kickedMessage = KickedMessage.readFast(StyledStringReader.of(this.original.styledString()));
            if (kickedMessage.isReal()) return kickedMessage;
        }

        return Message.FAILURE;
    }
}
