package dev.yatloaf.modkrowd.cubekrowd.message.cache;

import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.ThemedCache;
import dev.yatloaf.modkrowd.cubekrowd.message.Message;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.cubekrowd.subserver.TeamSet;

// TODO: Remove inheritance, currently an intermediate hack
public abstract class MessageCache extends ThemedCache {
    private boolean blocked = false;
    private int backgroundTint = 0;
    private Message result;

    public MessageCache(TextCache original) {
        super(original);
    }

    public static MessageCache of(TextCache message, Subserver subserver) {
        if (subserver == Subservers.PENDING) {
            // Slight hack to play more nicely with PENDING
            return new FakeMessageCache(message);
        } else if (subserver.hasChattymotes) {
            return new MainMessageCache(message);
        } else if (subserver.teams == TeamSet.MISSILEWARS) {
            return new MissileWarsMessageCache(message, subserver);
        } else if (subserver.teams == TeamSet.FISHSLAP) {
            return new FishslapMessageCache(message);
        } else if (subserver.isMinigame) {
            return new MinigameMessageCache(message, subserver);
        } else if (subserver.isCubeKrowd) {
            return new CubeKrowdMessageCache(message);
        } else {
            return new NoneMessageCache(message);
        }
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

    protected abstract Message createResult();
}
