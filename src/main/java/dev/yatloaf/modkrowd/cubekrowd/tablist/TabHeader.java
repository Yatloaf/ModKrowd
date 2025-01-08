package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.RankName;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public record TabHeader(RankName rankName, TabHeaderTime time) {
    public static final StyledString PREFIX1 = StyledString.concat(
            StyledString.fromString("----------------[", CKColor.DARK_GRAY.style.withStrikethrough(true)),
            StyledString.SPACE
    );

    public static final StyledString INFIX1 = StyledString.fromString("Welcome ", CKColor.GRAY.style);

    public static final StyledString SUFFIX1 = StyledString.concat(
            StyledString.SPACE,
            StyledString.fromString("]----------------", CKColor.DARK_GRAY.style.withStrikethrough(true))
    );

    public static final StyledString PREFIX2 = StyledString.fromString("Current Time (UTC): ", CKColor.GOLD.style);

    public static TabHeader readSoft(StyledStringReader source) {
        source.skipUntilAfter("Welcome ");
        RankName rankName = RankName.readSoft(source);
        source.skipUntilAfter("(UTC): ");
        TabHeaderTime time = TabHeaderTime.read(source);

        return new TabHeader(rankName, time);
    }
}
