package dev.yatloaf.modkrowd.cubekrowd.message.cache;

import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.message.Message;
import dev.yatloaf.modkrowd.cubekrowd.message.MissileWarsDeathMessage;
import dev.yatloaf.modkrowd.cubekrowd.message.MissileWarsGameEndMessage;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public class MissileWarsMessageCache extends MinigameMessageCache {
    private MissileWarsDeathMessage missileWarsDeathMessage;
    private MissileWarsGameEndMessage missileWarsGameEndMessage;

    public MissileWarsMessageCache(TextCache original, Subserver subserver) {
        super(original, subserver);
    }

    @Override
    protected Message createResult() {
        MissileWarsDeathMessage missileWarsDeathMessage = this.missileWarsDeathMessageFast();
        if (missileWarsDeathMessage.isReal()) {
            return missileWarsDeathMessage;
        }

        MissileWarsGameEndMessage missileWarsGameEndMessage = this.missileWarsGameEndMessageFast();
        if (missileWarsGameEndMessage.isReal()) {
            return missileWarsGameEndMessage;
        }

        return super.createResult();
    }

    public final MissileWarsDeathMessage missileWarsDeathMessageFast() {
        if (this.missileWarsDeathMessage == null) {
            this.missileWarsDeathMessage = MissileWarsDeathMessage.parseFast(this.original.text().getContents());
        }
        return this.missileWarsDeathMessage;
    }

    public final MissileWarsGameEndMessage missileWarsGameEndMessageFast() {
        if (this.missileWarsGameEndMessage == null) {
            this.missileWarsGameEndMessage = MissileWarsGameEndMessage.readFast(StyledStringReader.of(this.original.styledString()));
        }
        return this.missileWarsGameEndMessage;
    }
}
