package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.SelfPlayer;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.client.network.PlayerListEntry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record MainTabList(EntryCache[] entries, EntryCache[] players, EntryCache self, Set<Subserver> listedSubservers, boolean isReal) implements TabList {
    public static final EntryCache[] EMPTY_ENTRIES = new EntryCache[0];
    public static final MainTabList FAILURE = new MainTabList(EMPTY_ENTRIES, EMPTY_ENTRIES, null, Set.of(), false);

    private static final StyledString SU2 = StyledString.concat(
            StyledString.fromString("[", CKColor.GRAY.style),
            StyledString.fromString("su2", CKColor.YELLOW.style),
            StyledString.fromString("]", CKColor.GRAY.style)
    );

    private static final IntSet SPECIAL_INDEXES = IntSet.of(0, 17, 18, 19, 20, 40, 60);

    public static MainTabList parseFast(TabListCache source) {
        List<PlayerListEntry> playerListEntries = source.playerListEntries();
        if (playerListEntries.size() < 80) return FAILURE;

        MainTabColumn currentColumn = MainTabColumn.FAILURE;
        Set<Subserver> listedSubservers = new HashSet<>(4);

        EntryCache[] entries = new EntryCache[80];
        List<EntryCache> playersBuilder = new ArrayList<>();
        String selfName = SelfPlayer.username();
        EntryCache self = null;
        for (int index = 0; index < 80; index++) {
            TextCache name = source.getPlayerName(index);
            boolean isPlayer;
            EntryCache entryCache;
            if (SPECIAL_INDEXES.contains(index)) {
                // Pre-parsed

                entryCache = new EntryCache(name, index, playerListEntries.get(index).getLatency(), currentColumn.subserver());
                TabEntry result = entryCache.result();
                if (result instanceof MainTabColumn mainTabColumn) {
                    if (!mainTabColumn.isReal()) return FAILURE;
                    currentColumn = mainTabColumn;
                    Subserver subserver = currentColumn.subserver();
                    listedSubservers.add(subserver);
                    if (subserver == Subservers.SURVIVAL_AMBIGUOUS) {
                        listedSubservers.add(Subservers.SURVIVAL);
                        listedSubservers.add(Subservers.SURVIVAL2);
                    }
                }
                isPlayer = entryCache.isPlayer();
            } else {
                // Lazy shortcut

                isPlayer = !name.string().isBlank() && !name.styledString().contains(___AND);
                entryCache = new EntryCache(
                        name, index, playerListEntries.get(index).getLatency(), isPlayer, currentColumn.subserver()
                );
            }
            entries[index] = entryCache;
            if (isPlayer) {
                playersBuilder.add(entryCache);
                if (name.string().equals(selfName)) {
                    self = entryCache;
                }
            }
        }
        EntryCache[] players = playersBuilder.toArray(EntryCache[]::new);

        return new MainTabList(entries, players, self, listedSubservers, true);
    }

    @Override
    public boolean listsSubserver(Subserver subserver) {
        return this.listedSubservers.contains(subserver);
    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    public static class EntryCache extends TabEntryCache {
        public final int index;
        public final boolean isPlayer;

        private Subserver subserver;
        private TabEntry result;

        public EntryCache(TextCache name, int index, int latency, Subserver subserver) {
            super(name, latency);
            this.index = index;
            this.subserver = subserver;
            this.isPlayer = this.result() instanceof MainTabName;
        }

        public EntryCache(TextCache name, int index, int latency, boolean isPlayer, Subserver subserver) {
            super(name, latency);
            this.index = index;
            this.isPlayer = isPlayer;
            this.subserver = subserver;
        }

        @Override
        public TabEntry result() {
            if (this.result == null) {
                this.result = this.createResult();
            }
            return this.result;
        }

        protected TabEntry createResult() {
            if (this.index % 20 == 0) {
                return MainTabColumn.readFast(StyledStringReader.of(this.original.styledString()));
            }

            // Misaligned info NOT hardcoded to BuildTeam
            if (this.index == 19 || this.index == 18) {
                TabPing tabPing = TabPing.readFast(StyledStringReader.of(this.original.styledString()));
                if (tabPing.isReal()) {
                    return tabPing;
                }
            }
            if (this.index == 18 || this.index == 17) {
                MainTabPlayers mainTabPlayers = MainTabPlayers.readFast(StyledStringReader.of(this.original.styledString()));
                if (mainTabPlayers.isReal()) {
                    return mainTabPlayers;
                }
            }

            MainTabName mainTabName = MainTabName.readFast(StyledStringReader.of(this.original.styledString()));
            if (mainTabName.isReal()) {
                return mainTabName;
            }

            return TabEntry.FAILURE;
        }

        @Override
        public boolean isPlayer() {
            return this.isPlayer;
        }

        @Override
        public Subserver subserver() {
            if (this.subserver == Subservers.SURVIVAL_AMBIGUOUS) {
                this.subserver = this.original.styledString().endsWith(SU2) ? Subservers.SURVIVAL2 : Subservers.SURVIVAL;
            }
            return this.subserver;
        }
    }
}
