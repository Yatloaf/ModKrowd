package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;
import net.minecraft.client.multiplayer.PlayerInfo;

import java.util.List;
import java.util.function.Function;

public record GameLobbyTabList(EntryCache[] entries, boolean isReal) implements TabList {
    public static final EntryCache[] EMPTY_ENTRIES = new EntryCache[0];
    public static final GameLobbyTabList FAILURE = new GameLobbyTabList(EMPTY_ENTRIES, false);

    public static GameLobbyTabList parseFast(TabListCache source) {
        List<PlayerInfo> playerListEntries = source.playerListEntries();
        if (playerListEntries.size() < 80) return FAILURE;

        StyledString styledString40 = source.getPlayerName(40).styledString();
        if (TabCentered.parse(styledString40, GameTabLabel.ARCKADE::parseSpecific).content() != GameTabLabel.ARCKADE) return FAILURE;

        StyledString styledString21 = source.getPlayerName(21).styledString();
        if (TabCentered.parse(styledString21, GameTabLabel.MODE::parseSpecific).content() != GameTabLabel.MODE) return FAILURE;

        EntryCache[] entries = new EntryCache[playerListEntries.size()];
        for (int index = 0; index < playerListEntries.size(); index++) {
            PlayerInfo playerListEntry = playerListEntries.get(index);
            EntryCache entryCache = new EntryCache(source.getPlayerName(index), index, playerListEntry.getLatency());
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
        public final int index;

        private TabEntry result;

        public EntryCache(TextCache name, int index, int latency) {
            super(name, latency);
            this.index = index;
        }

        @Override
        public TabEntry result() {
            if (this.result == null) {
                this.result = this.createResult();
            }
            return this.result;
        }

        protected TabEntry createResult() {
            if ((this.index + 1) % 20 <= 2) { // Top 2 rows and bottom 1 row
                if (this.index == 19) {
                     return TabPing.readFast(StyledStringReader.of(this.original.styledString()));
                } else {
                    return TabCentered.parse(this.original.styledString(), labelForIndex(this.index)::parseSpecific);
                }
            } else {
                return TabCentered.parse(this.original.styledString(), contentParserForIndex(this.index));
            }
        }

        protected static GameTabLabel labelForIndex(int index) {
            return switch (index) {
                case 20 -> GameTabLabel.WELCOME_TO;
                case 40 -> GameTabLabel.ARCKADE;
                case 1 -> GameTabLabel.SERVER;
                case 21 -> GameTabLabel.MODE;
                case 41 -> GameTabLabel.PLAYERS;
                case 61 -> GameTabLabel.STATUS;
                default -> GameTabLabel.EMPTY;
            };
        }

        protected static Function<StyledString, TabEntry> contentParserForIndex(int index) {
            if (index < 40) {
                // Left 2 columns
                if (index < 20) {
                    return GameTabSubserver::parse; // Leftmost column
                } else {
                    return source -> GameTabLabel.UNDER.matches(source)
                            ? GameTabLabel.UNDER
                            : GameTabMode.parse(source); // Middle left column
                }
            } else {
                // Right 2 columns
                if (index < 60) {
                    return source -> GameTabLabel.MAINTENANCE.matches(source)
                            ? GameTabLabel.MAINTENANCE
                            : GameTabPlayers.readSoft(StyledStringReader.of(source)); // Middle right column
                } else {
                    return GameTabStatus::parse; // Rightmost column
                }
            }
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
