package dev.yatloaf.modkrowd.cubekrowd.common;

import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public enum RankPipes {
    NONE(StyledString.EMPTY),
    EMERALD(StyledString.fromString("|", CKColor.GREEN.style)),
    UNKNOWN(StyledString.EMPTY); // Never used

    public final StyledString pipe;

    RankPipes(StyledString pipe) {
        this.pipe = pipe;
    }

    public static RankPipes read(StyledStringReader source) {
        if (source.skipIfNext(EMERALD.pipe)) {
            return EMERALD;
        } else {
            return NONE;
        }
    }

    public boolean isReal() {
        return this != UNKNOWN;
    }
}
