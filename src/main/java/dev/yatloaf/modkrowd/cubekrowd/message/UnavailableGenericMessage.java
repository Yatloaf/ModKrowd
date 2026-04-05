package dev.yatloaf.modkrowd.cubekrowd.message;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public record UnavailableGenericMessage(Subserver subserver, boolean isReal) implements UnavailableMessage {
    public static final UnavailableGenericMessage FAILURE = new UnavailableGenericMessage(Subservers.NONE, false);

    public static final StyledString PREFIX = StyledString.fromString("Unable to connect you to ", CKColor.RED.style);
    public static final StyledString SUFFIX = StyledString.fromString("Please try again later.", CKColor.RED.style);

    public static UnavailableGenericMessage readFast(StyledStringReader source) {
        if (!source.skipIfNext(PREFIX)) return FAILURE;

        Subserver subserver = Subservers.fromId(source.readUntil(".").toUnstyledString());
        if (!subserver.isReal) return FAILURE;

        source.skipUntilAfter(" ");
        if (!source.skipIfNext(SUFFIX)) return FAILURE;

        return new UnavailableGenericMessage(subserver, true);
    }
}
