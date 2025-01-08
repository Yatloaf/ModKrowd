package dev.yatloaf.modkrowd.cubekrowd.message;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.subserver.RealSubserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public record WhereamiMessage(Subserver subserver, boolean isReal) implements Message {
    public static final WhereamiMessage FAILURE = new WhereamiMessage(Subservers.NONE, false);

    public static final StyledString WHEREAMI_RESPONSE_PREFIX = StyledString.fromString("You are currently on", CKColor.GOLD.style);

    public static WhereamiMessage readFast(StyledStringReader source) {
        if (!source.skipIfNext(WHEREAMI_RESPONSE_PREFIX)) return FAILURE;
        source.skipUntilAfter(" ");
        Subserver subserver = Subservers.fromId(source.readAll().toUnstyledString());
        if (subserver instanceof RealSubserver) {
            return new WhereamiMessage(subserver, true);
        } else {
            return FAILURE;
        }
    }
}
