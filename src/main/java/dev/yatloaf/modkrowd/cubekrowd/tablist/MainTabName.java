package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.Afk;
import dev.yatloaf.modkrowd.cubekrowd.common.RankName;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public record MainTabName(Afk afk, RankName rankName, boolean isReal) implements TabEntry {
    public static final MainTabName FAILURE = new MainTabName(Afk.FALSE, RankName.FAILURE, false);

    public static MainTabName readFast(StyledStringReader source) {
        Afk afk = Afk.read(source);
        if (!afk.isReal()) return FAILURE;

        RankName rankName = RankName.readFast(source);
        if (!rankName.isReal()) return FAILURE;

        return new MainTabName(afk, rankName, true);
    }

    public StyledString appearance() {
        return StyledString.concat(
                this.afk.star,
                this.rankName.appearance()
        );
    }
}
