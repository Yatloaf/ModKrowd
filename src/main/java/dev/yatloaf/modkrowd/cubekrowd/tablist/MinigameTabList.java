package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.SelfPlayer;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.subserver.MinigameSubserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;
import net.minecraft.client.network.PlayerListEntry;

import java.util.ArrayList;
import java.util.List;

public record MinigameTabList(EntryCache[] entries, EntryCache[] players, EntryCache self, Subserver yourGame, boolean isReal) implements TabList {
    public static final EntryCache[] EMPTY_ENTRIES = new EntryCache[0];
    public static final MinigameTabList FAILURE = new MinigameTabList(EMPTY_ENTRIES, EMPTY_ENTRIES, null, Subservers.NONE, false);

    public static final StyledString YOUR_GAME = StyledString.fromString("Your game", CKColor.GOLD.style.withBold(true));

    public static MinigameTabList parseFast(TabListCache source) {
        List<PlayerListEntry> playerListEntries = source.playerListEntries();
        if (playerListEntries.size() < 80) return FAILURE;

        StyledString styledString40 = source.getPlayerName(40).styledString();
        if (!styledString40.strip().equals(ARCKADE)) return FAILURE;

        StyledString styledString61 = source.getPlayerName(61).styledString();
        if (!styledString61.strip().equals(YOUR_GAME)) return FAILURE;

        boolean isLoaded = ModKrowd.currentSubserver.isReal();
        Subserver yourGame = isLoaded ? ModKrowd.currentSubserver : Subservers.UNKNOWN;

        EntryCache[] entries = new EntryCache[playerListEntries.size()];
        List<EntryCache> playersBuilder = new ArrayList<>();
        String selfName = SelfPlayer.username();
        EntryCache self = null;
        for (int index = 0; index < playerListEntries.size(); index++) {
            PlayerListEntry playerListEntry = playerListEntries.get(index);
            TextCache name = source.getPlayerName(index);
            boolean isPlayer = isLoaded && index >= 62 && !name.string().isBlank() && !name.styledString().contains(___AND);

            EntryCache entryCache = new EntryCache(name, index, playerListEntry.getLatency(), yourGame, isPlayer);
            entries[index] = entryCache;
            if (isPlayer) {
                playersBuilder.add(entryCache);
                if (name.string().equals(selfName)) {
                    self = entryCache;
                }
            }
        }
        EntryCache[] players = playersBuilder.toArray(EntryCache[]::new);

        return new MinigameTabList(entries, players, self, yourGame, true);
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
        public final int index;
        public final Subserver subserver;
        public final boolean isPlayer;

        private TabEntry result;

        public EntryCache(TextCache name, int index, int latency, Subserver subserver, boolean isPlayer) {
            super(name, latency);
            this.index = index;
            this.subserver = subserver;
            this.isPlayer = isPlayer;
        }

        @Override
        public TabEntry result() {
            if (this.result == null) {
                this.result = this.createResult();
            }
            return this.result;
        }

        protected TabEntry createResult() {
            if (this.isPlayer && this.subserver instanceof MinigameSubserver minigameSubserver) {
                MinigameTabName minigameTabName = MinigameTabName.readFast(StyledStringReader.of(this.original.styledString()), minigameSubserver);
                if (minigameTabName.isReal()) {
                    return minigameTabName;
                }
            }

            return TabEntry.FAILURE;
        }

        @Override
        public boolean isPlayer() {
            return this.isPlayer;
        }

        @Override
        public Subserver subserver() {
            return this.subserver;
        }
    }
}
