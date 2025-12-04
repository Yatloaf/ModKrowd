package dev.yatloaf.modkrowd.cubekrowd.message;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public record KickedMessage(Subserver subserver, String reason, boolean isReal) implements Message {
    public static final KickedMessage FAILURE = new KickedMessage(Subservers.NONE, "", false);

    public static final StyledString CAG2 = StyledString.fromString("Kicked whilst connecting to ", CKColor.RED.style);

    public static KickedMessage readFast(StyledStringReader source) {
        if (!source.skipIfNext(CAG2)) return FAILURE;

        Subserver subserver = Subservers.fromId(source.readUntil(":").toUnstyledString());
        if (!subserver.isReal) return FAILURE;

        source.skipUntilAfter(":");
        String reason = source.readAll().toUnstyledString();

        return new KickedMessage(subserver, reason, true);
    }
}
