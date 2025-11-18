package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import net.minecraft.client.multiplayer.PlayerInfo;

import java.util.List;

public record UnknownTabList(EntryCache[] entries) implements TabList {
    public static final EntryCache[] EMPTY_ENTRIES = new EntryCache[0];

    public static UnknownTabList parse(TabListCache source) {
        List<PlayerInfo> playerListEntries = source.playerListEntries();

        EntryCache[] entries = new EntryCache[playerListEntries.size()];
        for (int index = 0; index < entries.length; index++) {
            entries[index] = new EntryCache(source.getPlayerName(index), playerListEntries.get(index).getLatency());
        }

        return new UnknownTabList(entries);
    }

    @Override
    public TabEntryCache[] players() {
        return EMPTY_ENTRIES;
    }

    @Override
    public TabEntryCache self() {
        return null;
    }

    @Override
    public boolean listsSubserver(Subserver subserver) {
        return subserver == Subservers.UNKNOWN;
    }

    @Override
    public boolean isLoaded() {
        return false;
    }

    public static class EntryCache extends TabEntryCache {
        public EntryCache(TextCache name, int latency) {
            super(name, latency);
        }

        @Override
        public TabEntry result() {
            return TabEntry.FAILURE;
        }

        @Override
        public boolean isPlayer() {
            return true;
        }

        @Override
        public Subserver subserver() {
            return Subservers.UNKNOWN;
        }
    }
}
