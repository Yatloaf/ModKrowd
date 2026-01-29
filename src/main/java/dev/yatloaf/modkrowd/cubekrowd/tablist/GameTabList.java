package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.SelfPlayer;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

import java.util.ArrayList;
import java.util.List;

public record GameTabList(TabEntry[] entries, TabEntry[] players, TabEntry self, Subserver yourGame, boolean isReal) implements TabList {
    public static final GameTabList FAILURE = new GameTabList(TabEntry.EMPTY, TabEntry.EMPTY, null, Subservers.NONE, false);

    public static GameTabList parseFast(TabListCache source) {
        if (source.entries.length < 80) return FAILURE;

        Subserver yourGame = Subservers.UNKNOWN;

        TabEntry[] entries = new TabEntry[80];
        List<TabEntry> playersBuilder = new ArrayList<>();
        String selfName = SelfPlayer.username();
        TabEntry self = null;
        for (int index = 0; index < entries.length; index++) {
            TabEntryCache entryCache = source.entries[index];
            TabEntry entry = parseEntry(index, entryCache.name().styledString(), yourGame);

            switch (entry) {
                case GameTabColumn gameTabColumn -> {
                    if (!gameTabColumn.isReal()) return FAILURE;

                    yourGame = gameTabColumn.subserver();
                }
                case GameTabSubserver gameTabSubserver
                        when entryCache.icon == TabIcon.STONE_RIGHT_ARROW -> yourGame = gameTabSubserver.subserver();
                case TabLiteral tabLiteral
                        when !tabLiteral.isReal() -> { return FAILURE; }
                default -> {}
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

        return new GameTabList(entries, players, self, yourGame, true);
    }

    private static TabEntry parseEntry(int index, StyledString name, Subserver subserver) {
        if (index == 60) {
            // Criterion for successful parsing
            return GameTabColumn.parseFast(name, subserver);
        } else if (index % 20 == 0) {
            // Criterion for successful parsing
            return TabLiteral.SERVERS.parseSpecific(name);
        } else if (index == 18) {
            TabPlayers tabPlayers = TabPlayers.readFast(StyledStringReader.of(name));
            if (tabPlayers.isReal()) return tabPlayers;
        } else if (index == 19) {
            TabPing tabPing = TabPing.readFast(StyledStringReader.of(name));
            if (tabPing.isReal()) return tabPing;
        }

        TabLiteral empty = TabLiteral.EMPTY.parseSpecific(name);
        if (empty.isReal()) return empty;

        GameTabMinigame gameTabMinigame = GameTabMinigame.parseFast(name);
        if (gameTabMinigame.isReal()) return gameTabMinigame;

        GameTabSubserver gameTabSubserver = GameTabSubserver.readFast(StyledStringReader.of(name));
        if (gameTabSubserver.isReal()) return gameTabSubserver;

        MainTabName mainTabName = MainTabName.readFast(StyledStringReader.of(name), subserver);
        if (mainTabName.isReal()) return mainTabName;

        MinigameTabName minigameTabName = MinigameTabName.readFast(StyledStringReader.of(name), subserver);
        if (minigameTabName.isReal()) return minigameTabName;

        return TabEntry.FAILURE;
    }

    @Override
    public boolean listsSubserver(Subserver subserver) {
        return subserver == this.yourGame;
    }
}
