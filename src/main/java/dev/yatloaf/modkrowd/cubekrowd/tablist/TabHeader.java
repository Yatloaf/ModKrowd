package dev.yatloaf.modkrowd.cubekrowd.tablist;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.common.RankName;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public record TabHeader(RankName rankName, StyledString leftHyphens, StyledString rightHyphens, StyledString cubeKrowd, TabHeaderTime time, boolean isReal) {
    // "----------------[" and "]----------------" have had the number of hyphens change before,
    // so just read and save them. Also save "CubeKrowd" because it's animated

    public static final TabHeader FAILURE = new TabHeader(RankName.FAILURE, StyledString.EMPTY, StyledString.EMPTY, StyledString.EMPTY, TabHeaderTime.FAILURE, false);

    public static final StyledString INFIX1 = StyledString.fromString("Welcome", CKColor.GRAY.style);

    public static final StyledString PREFIX2 = StyledString.fromString("to", CKColor.GRAY.style);

    // This got changed, just accept both
    public static final StyledString PREFIX3 = StyledString.fromString("Current Time (UTC):", CKColor.GOLD.style);
    public static final StyledString PREFIX3B = StyledString.fromString("Current time (UTC):", CKColor.GOLD.style);

    public static TabHeader readFast(StyledStringReader source) {
        source.skipSpace();
        StyledString leftHyphens = source.readUntilAfter("[");
        if (leftHyphens.isEmpty()) return FAILURE;

        source.skipSpace();
        if (!source.skipIfNext(INFIX1)) return FAILURE;

        source.skipSpace();
        RankName rankName = RankName.readFast(source);
        if (!rankName.isReal()) return FAILURE;

        source.skipSpace();
        StyledString rightHyphens = source.readUntilSpace();
        if (rightHyphens.isEmpty()) return FAILURE;

        source.skipSpace();
        if (!source.skipIfNext(PREFIX2)) return FAILURE;

        source.skipSpace();
        StyledString cubeKrowd = source.readUntilSpace();
        if (cubeKrowd.isEmpty()) return FAILURE;

        source.skipSpace();
        if (!source.skipIfNext(PREFIX3) && !source.skipIfNext(PREFIX3B)) return FAILURE;

        source.skipSpace();
        TabHeaderTime time = TabHeaderTime.readFast(source);
        if (!time.isReal()) return FAILURE;

        return new TabHeader(rankName, leftHyphens.isolate(), rightHyphens.isolate(), cubeKrowd.isolate(), time, true);
    }
}
