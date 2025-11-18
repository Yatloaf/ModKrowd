package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.cubekrowd.common.SelfPlayer;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.subserver.MinigameSubserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabEntryCache;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;
import net.minecraft.client.multiplayer.PlayerInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public record MinigameTabList(EntryCache[] entries, EntryCache[] players, EntryCache self, Subserver yourGame, boolean isReal) implements TabList {
    public static final EntryCache[] EMPTY_ENTRIES = new EntryCache[0];
    public static final MinigameTabList FAILURE = new MinigameTabList(EMPTY_ENTRIES, EMPTY_ENTRIES, null, Subservers.NONE, false);

    public static MinigameTabList parseFast(TabListCache source) {
        List<PlayerInfo> playerListEntries = source.playerListEntries();
        if (playerListEntries.size() < 80) return FAILURE;

        StyledString styledString40 = source.getPlayerName(40).styledString();
        if (TabCentered.parse(styledString40, GameTabLabel.ARCKADE::parseSpecific).content() != GameTabLabel.ARCKADE) return FAILURE;

        StyledString styledString61 = source.getPlayerName(61).styledString();
        if (TabCentered.parse(styledString61, GameTabLabel.YOUR_GAME::parseSpecific).content() != GameTabLabel.YOUR_GAME) return FAILURE;

        boolean isLoaded = ModKrowd.currentSubserver.isReal();
        Subserver yourGame = isLoaded ? ModKrowd.currentSubserver : Subservers.UNKNOWN;

        EntryCache[] entries = new EntryCache[playerListEntries.size()];
        List<EntryCache> playersBuilder = new ArrayList<>();
        String selfName = SelfPlayer.username();
        EntryCache self = null;
        for (int index = 0; index < playerListEntries.size(); index++) {
            PlayerInfo playerListEntry = playerListEntries.get(index);
            TextCache name = source.getPlayerName(index);
            boolean isPlayer = isLoaded && index >= 62 && index <= 79 && !name.string().isBlank() && !name.styledString().contains(___AND);

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
            if (this.isPlayer) {
                if (this.subserver instanceof MinigameSubserver minigameSubserver) {
                    return MinigameTabName.readFast(StyledStringReader.of(this.original.styledString()), minigameSubserver);
                } else {
                    return TabEntry.FAILURE;
                }
            } else if ((this.index + 1) % 20 <= 2) { // Top 2 rows and bottom 1 row
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
                case 21 -> GameTabLabel.PLAYERS;
                case 41 -> GameTabLabel.STATUS;
                case 61 -> GameTabLabel.YOUR_GAME;
                default -> GameTabLabel.EMPTY;
            };
        }

        protected static Function<StyledString, TabEntry> contentParserForIndex(int index) {
            if (index < 40) {
                if (index < 20) {
                    return GameTabSubserver::parse;
                } else {
                    return source -> GameTabLabel.UNDER.matches(source)
                            ? GameTabLabel.UNDER
                            : GameTabPlayers.readSoft(StyledStringReader.of(source));
                }
            } else {
                return source -> GameTabLabel.MAINTENANCE.matches(source)
                        ? GameTabLabel.MAINTENANCE
                        : GameTabStatus.parse(source);
            }
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
