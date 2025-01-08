package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
import dev.yatloaf.modkrowd.util.text.StyledString;
import net.minecraft.client.network.PlayerListEntry;

import java.util.List;

public record GameLobbyTabList(EntryCache[] entries, boolean isReal) implements TabList {
    public static final EntryCache[] EMPTY_ENTRIES = new EntryCache[0];
    public static final GameLobbyTabList FAILURE = new GameLobbyTabList(EMPTY_ENTRIES, false);

    public static final StyledString MODE = StyledString.fromString("Mode", CKColor.GOLD.style.withBold(true));

    public static GameLobbyTabList parseFast(TabListCache source) {
        List<PlayerListEntry> playerListEntries = source.playerListEntries();
        if (playerListEntries.size() < 80) return FAILURE;

        StyledString styledString40 = source.getPlayerName(40).styledString();
        if (!styledString40.strip().equals(ARCKADE)) return FAILURE;

        StyledString styledString21 = source.getPlayerName(21).styledString();
        if (!styledString21.strip().equals(MODE)) return FAILURE;

        EntryCache[] entries = new EntryCache[playerListEntries.size()];
        for (int index = 0; index < playerListEntries.size(); index++) {
            PlayerListEntry playerListEntry = playerListEntries.get(index);
            EntryCache entryCache = new EntryCache(source.getPlayerName(index), playerListEntry.getLatency());
            entries[index] = entryCache;
        }

        return new GameLobbyTabList(entries, true);
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
        return false;
    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    public static class EntryCache extends TabEntryCache {
        public EntryCache(TextCache name, int latency) {
            super(name, latency);
        }

        @Override
        public TabEntry result() { // TODO
            return TabEntry.FAILURE;
        }

        @Override
        public boolean isPlayer() {
            return false;
        }

        @Override
        public Subserver subserver() {
            return Subservers.GAMELOBBY;
        }
    }
}
