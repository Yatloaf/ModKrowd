package dev.yatloaf.modkrowd.cubekrowd.message.cache;

import dev.yatloaf.modkrowd.cubekrowd.message.Message;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.ThemedCache;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.subserver.CubeKrowdSubserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.FakeSubserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.FishslapSubserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.MainSubserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.MinigameSubserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.MissileWarsSubserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;

public abstract class MessageCache extends ThemedCache {
    private boolean blocked = false;
    private Message result;

    public MessageCache(TextCache original) {
        super(original);
    }

    public static MessageCache of(TextCache message, Subserver subserver) {
        return switch (subserver) {
            case FakeSubserver ignored -> new FakeMessageCache(message); // Slight hack to play more nicely with PENDING
            case MainSubserver ignored -> new MainMessageCache(message);
            case MissileWarsSubserver missileWarsSubserver -> new MissileWarsMessageCache(message, missileWarsSubserver);
            case FishslapSubserver ignored -> new FishslapMessageCache(message);
            case MinigameSubserver minigameSubserver -> new MinigameMessageCache(message, minigameSubserver);
            case CubeKrowdSubserver ignored -> new CubeKrowdMessageCache(message);
            default -> new NoneMessageCache(message);
        };
    }

    public boolean blocked() {
        return this.blocked;
    }

    public void setBlocked(boolean value) {
        this.blocked = value;
    }

    public final Message result() {
        if (this.result == null) {
            this.result = this.createResult();
        }
        return this.result;
    }

    protected abstract Message createResult();
}
