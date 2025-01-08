package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.util.Util;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.Arrays;
import java.util.stream.Collectors;

public record MainTabColumn(Subserver subserver, String subserverName, int[] playerCounts, boolean online,
                            boolean isReal) implements TabEntry {
    public static final MainTabColumn FAILURE = new MainTabColumn(Subservers.UNKNOWN, "", new int[0], false, false);

    public static MainTabColumn readFast(StyledStringReader source) {
        String subserverName = source.readUntil(" [").toUnstyledString();
        Subserver subserver = Subservers.fromTabName(subserverName);
        if (subserver == Subservers.UNKNOWN) return FAILURE;

        source.skipUntilAfter("[");
        int[] playerCounts;
        boolean online;

        if (source.skipIfNext("!")) {
            playerCounts = new int[]{0};
            online = false;
            source.skipUntilAfter("]");
        } else {
            IntList playerCountsList = new IntArrayList();
            while (!source.skipIfNext("]")) {
                int playerCount = Util.parseIntOr(source.readUntilAny("|", "]").toUnstyledString(), -1);
                if (playerCount == -1) return FAILURE;
                playerCountsList.add(playerCount);
                source.skipIfNext("|");
            }
            playerCounts = playerCountsList.toIntArray();
            online = true;
        }

        return new MainTabColumn(subserver, subserverName, playerCounts, online, true);
    }

    public StyledString appearance() {
        if (this.online) {
            return StyledString.fromString(
                    this.subserverName
                    + " [" + Arrays.stream(this.playerCounts).mapToObj(Integer::toString).collect(Collectors.joining("|")) + "]",
                    CKColor.GOLD.style.withBold(true)
            );
        } else {
            return StyledString.fromString(this.subserverName + " [!]", CKColor.RED.style.withBold(true));
        }
    }
}
