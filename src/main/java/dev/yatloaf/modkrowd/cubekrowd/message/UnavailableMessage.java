package dev.yatloaf.modkrowd.cubekrowd.message;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public record UnavailableMessage(Subserver subserver, boolean isReal) implements Message {
    public static final UnavailableMessage FAILURE = new UnavailableMessage(Subservers.NONE, false);

    public static final StyledString PREFIX = StyledString.fromString("Unable to connect you to ", CKColor.RED.style);
    public static final StyledString SUFFIX = StyledString.fromString("Please try again later.", CKColor.RED.style);

    public static UnavailableMessage readFast(StyledStringReader source) {
        if (!source.skipIfNext(PREFIX)) return FAILURE;

        Subserver subserver = Subservers.fromId(source.readUntil(".").toUnstyledString());
        if (!subserver.isReal) return FAILURE;

        source.skipUntilAfter(" ");
        if (!source.skipIfNext(SUFFIX)) return FAILURE;

        return new UnavailableMessage(subserver, true);
    }
}
