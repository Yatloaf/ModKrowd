package dev.yatloaf.modkrowd.cubekrowd.message;

import dev.yatloaf.modkrowd.cubekrowd.common.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.common.ThemedCache;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;
import net.minecraft.client.GuiMessage;

import java.util.ArrayList;
import java.util.List;

public class MessageCache extends ThemedCache {
    public final Subserver subserver;
    public final List<GuiMessage.Line> lines = new ArrayList<>();

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

            UnavailableGenericMessage unavailableGenericMessage = UnavailableGenericMessage.readFast(StyledStringReader.of(this.original.styledString()));
            if (unavailableGenericMessage.isReal()) return unavailableGenericMessage;

            UnavailableReasonMessage unavailableReasonMessage = UnavailableReasonMessage.readFast(StyledStringReader.of(this.original.styledString()));
            if (unavailableReasonMessage.isReal()) return unavailableReasonMessage;
        }

        return Message.FAILURE;
    }
}
