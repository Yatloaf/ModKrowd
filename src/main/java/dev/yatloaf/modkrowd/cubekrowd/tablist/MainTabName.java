package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.Afk;
import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.RankName;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public record MainTabName(Afk afk, RankName rankName, Subserver subserver, boolean isReal) implements TabEntry {
    public static final MainTabName FAILURE = new MainTabName(Afk.FALSE, RankName.FAILURE, Subservers.UNKNOWN, false);

    private static final StyledString SU1 = StyledString.concat(
            StyledString.fromString("[", CKColor.GRAY.style),
            StyledString.fromString("su1", CKColor.AQUA.style),
            StyledString.fromString("]", CKColor.GRAY.style)
    );
    private static final StyledString SU2 = StyledString.concat(
            StyledString.fromString("[", CKColor.GRAY.style),
            StyledString.fromString("su2", CKColor.YELLOW.style),
            StyledString.fromString("]", CKColor.GRAY.style)
    );

    public static MainTabName readFast(StyledStringReader source, Subserver subserver) {
        Afk afk = Afk.read(source);
        if (!afk.isReal()) return FAILURE;

        RankName rankName = RankName.readFast(source);
        if (!rankName.isReal()) return FAILURE;

        if (subserver == Subservers.SURVIVAL_AMBIGUOUS) {
            source.skipUntilAfter(" ");
            if (source.skipIfNext(SU2)) {
                subserver = Subservers.SURVIVAL2;
            } else {
                source.skipIfNext(SU1);
                subserver = Subservers.SURVIVAL;
            }
        }

        if (!source.isAtEnd()) return FAILURE;

        return new MainTabName(afk, rankName, subserver, true);
    }

    public StyledString appearance() {
        return StyledString.concat(
                this.afk.star,
                this.rankName.appearance()
        );
    }

    @Override
    public StyledString playerName() {
        return this.rankName.name();
    }

    @Override
    public Subserver playerSubserver() {
        return this.subserver;
    }
}
