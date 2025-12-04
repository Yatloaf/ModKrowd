package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.cubekrowd.common.SelfPlayer;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import net.minecraft.client.multiplayer.PlayerInfo;

import java.util.List;

public record VanillaTabList(EntryCache[] entries, EntryCache[] players, EntryCache self, Subserver yourGame, boolean isReal) implements TabList {
    private static final EntryCache[] EMPTY_ENTRIES = new EntryCache[0];
    public static final VanillaTabList FAILURE = new VanillaTabList(EMPTY_ENTRIES, EMPTY_ENTRIES, null, Subservers.NONE, false);

    public static VanillaTabList parseFast(TabListCache source) {
        List<PlayerInfo> playerListEntries = source.playerListEntries();
        if (playerListEntries.isEmpty()) {
            return FAILURE;
        }

        // Fake tab list players either have an empty name or one containing "~"
        String firstProfileName = playerListEntries.getFirst().getProfile().name();
        if (firstProfileName.isBlank() || firstProfileName.contains("~")) {
            return FAILURE;
        }

        boolean isLoaded = ModKrowd.currentSubserver.isReal;
        Subserver yourGame = isLoaded ? ModKrowd.currentSubserver : Subservers.UNKNOWN;

        EntryCache[] entries = new EntryCache[playerListEntries.size()];
        String selfName = SelfPlayer.username();
        EntryCache self = null;
        for (int index = 0; index < playerListEntries.size(); index++) {
            PlayerInfo playerListEntry = playerListEntries.get(index);
            TextCache name = source.getPlayerName(index);
            EntryCache entryCache = new EntryCache(name, playerListEntry.getLatency(), yourGame);
            entries[index] = entryCache;
            if (name.string().equals(selfName)) {
                self = entryCache;
            }
        }
        EntryCache[] players = isLoaded ? entries : EMPTY_ENTRIES;

        return new VanillaTabList(entries, players, self, yourGame, true);
    }

    @Override
    public boolean listsSubserver(Subserver subserver) {
        return subserver == this.yourGame;
    }

    @Override
    public boolean isLoaded() {
        return this.yourGame != Subservers.UNKNOWN;
    }

    public static class EntryCache extends TabEntryCache {
        public final Subserver subserver;

        public EntryCache(TextCache name, int latency, Subserver subserver) {
            super(name, latency);
            this.subserver = subserver;
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
            return this.subserver;
        }
    }
}
