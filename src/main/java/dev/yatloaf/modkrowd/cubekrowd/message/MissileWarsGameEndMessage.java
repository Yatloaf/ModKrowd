package dev.yatloaf.modkrowd.cubekrowd.message;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public record MissileWarsGameEndMessage(boolean isReal) implements Message {
    public static final MissileWarsGameEndMessage FAILURE = new MissileWarsGameEndMessage(false);

    private static final StyledString RESTART_MESSAGE = StyledString.fromString("Game will restart in 15 seconds...", CKColor.AQUA.style);

    public static MissileWarsGameEndMessage readFast(StyledStringReader source) {
        if (source.readAll().equals(RESTART_MESSAGE)) {
            return new MissileWarsGameEndMessage(true);
        } else {
            return FAILURE;
        }
    }
}
