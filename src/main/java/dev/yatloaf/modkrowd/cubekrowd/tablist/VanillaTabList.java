package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.ModKrowd;
import dev.yatloaf.modkrowd.cubekrowd.common.SelfPlayer;
import dev.yatloaf.modkrowd.cubekrowd.common.cache.TextCache;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.cubekrowd.tablist.cache.TabListCache;

import java.util.ArrayList;
import java.util.List;

public record VanillaTabList(TabEntry[] entries, TabEntry[] players, TabEntry self, Subserver yourGame, boolean isReal) implements TabList {
    public static final VanillaTabList FAILURE = new VanillaTabList(TabEntry.EMPTY, TabEntry.EMPTY, null, Subservers.NONE, false);

    public static VanillaTabList parseFast(TabListCache source) {
        if (source.entries.length > 0) {
            // Fake tab list players either have an empty name or one containing "~"
            String firstProfileName = source.entries[0].profileName;
            if (firstProfileName.isBlank() || firstProfileName.contains("~")) {
                return FAILURE;
            }
        }

        return parseSoft(source);
    }

    public static VanillaTabList parseSoft(TabListCache source) {
        Subserver yourGame = ModKrowd.currentSubserver;

        TabEntry[] entries = new TabEntry[source.entries.length];
        List<TabEntry> playersBuilder = new ArrayList<>();
        String selfName = SelfPlayer.username();
        TabEntry self = null;
        for (int index = 0; index < entries.length; index++) {
            TextCache name = source.entries[index].name();
            TabEntry entry = new VanillaTabEntry(name, yourGame);

            if (!entry.playerName().isEmpty()) {
                playersBuilder.add(entry);
                // Unlike in the other tab lists, this skips conversion via StyledString
                if (name.string().equals(selfName)) {
                    self = entry;
                }
            }

            entries[index] = entry;
        }
        TabEntry[] players = playersBuilder.toArray(TabEntry[]::new);

        return new VanillaTabList(entries, players, self, yourGame, true);
    }

    @Override
    public boolean listsSubserver(Subserver subserver) {
        return subserver == this.yourGame;
    }
}
