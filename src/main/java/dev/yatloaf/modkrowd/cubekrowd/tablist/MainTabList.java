package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.SelfPlayer;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record MainTabList(TabEntry[] entries, TabEntry[] players, TabEntry self, Set<Subserver> listedSubservers, boolean isReal) implements TabList {
    public static final MainTabList FAILURE = new MainTabList(TabEntry.EMPTY, TabEntry.EMPTY, null, Set.of(), false);

    public static MainTabList parseFast(TabListCache source) {
        if (source.entries.length < 80) return FAILURE;

        Subserver currentSubserver = Subservers.UNKNOWN;
        Set<Subserver> listedSubservers = new HashSet<>(4);

        TabEntry[] entries = new TabEntry[80];
        List<TabEntry> playersBuilder = new ArrayList<>();
        String selfName = SelfPlayer.username();
        TabEntry self = null;
        for (int index = 0; index < entries.length; index++) {
            TabEntryCache entryCache = source.entries[index];
            TabEntry entry = parseEntry(index, entryCache.name().styledString(), currentSubserver);

            if (entry instanceof MainTabColumn mainTabColumn) {
                if (!mainTabColumn.isReal()) return FAILURE;

                currentSubserver = mainTabColumn.subserver();
                listedSubservers.add(currentSubserver);
                if (currentSubserver == Subservers.SURVIVAL_AMBIGUOUS) {
                    listedSubservers.add(Subservers.SURVIVAL);
                    listedSubservers.add(Subservers.SURVIVAL2);
                }
            }
            if (entry.isPlayer()) {
                playersBuilder.add(entry);
                if (entry.playerName().toUnstyledString().equals(selfName)) {
                    self = entry;
                }
            }

            entries[index] = entry;
        }
        TabEntry[] players = playersBuilder.toArray(TabEntry[]::new);

        return new MainTabList(entries, players, self, listedSubservers, true);
    }

    private static TabEntry parseEntry(int index, StyledString name, Subserver subserver) {
        if (index % 20 == 0) {
            // Criterion for successful parsing
            return MainTabColumn.readFast(StyledStringReader.of(name));
        } else if (index == 18) {
            TabPlayers tabPlayers = TabPlayers.readFast(StyledStringReader.of(name));
            if (tabPlayers.isReal()) return tabPlayers;
        } else if (index == 19) {
            TabPing tabPing = TabPing.readFast(StyledStringReader.of(name));
            if (tabPing.isReal()) return tabPing;
        }

        TabLiteral empty = TabLiteral.EMPTY.parseSpecific(name);
        if (empty.isReal()) return empty;

        MainTabName mainTabName = MainTabName.readFast(StyledStringReader.of(name), subserver);
        if (mainTabName.isReal()) return mainTabName;

        return TabEntry.FAILURE;
    }

    @Override
    public boolean listsSubserver(Subserver subserver) {
        return this.listedSubservers.contains(subserver);
    }
}
