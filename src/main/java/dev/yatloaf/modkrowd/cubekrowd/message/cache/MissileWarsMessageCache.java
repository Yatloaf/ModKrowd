package dev.yatloaf.modkrowd.cubekrowd.message.cache;

import dev.yatloaf.modkrowd.cubekrowd.message.Message;
import dev.yatloaf.modkrowd.cubekrowd.message.MissileWarsGameEndMessage;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.subserver.MissileWarsSubserver;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public class MissileWarsMessageCache extends MinigameMessageCache {
    private MissileWarsGameEndMessage missileWarsGameEndMessage;

    public MissileWarsMessageCache(TextCache original, MissileWarsSubserver subserver) {
        super(original, subserver);
    }

    @Override
    protected Message createResult() {
        MissileWarsGameEndMessage missileWarsGameEndMessage = this.missileWarsGameEndMessageFast();
        if (missileWarsGameEndMessage.isReal()) {
            return missileWarsGameEndMessage;
        }

        return super.createResult();
    }

    public final MissileWarsGameEndMessage missileWarsGameEndMessageFast() {
        if (this.missileWarsGameEndMessage == null) {
            this.missileWarsGameEndMessage = MissileWarsGameEndMessage.readFast(StyledStringReader.of(this.original.styledString()));
        }
        return this.missileWarsGameEndMessage;
    }
}
