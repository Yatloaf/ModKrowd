package dev.yatloaf.modkrowd.cubekrowd.message;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public record UnavailableMessage(boolean isReal) implements Message {
    public static final UnavailableMessage FAILURE = new UnavailableMessage(false);

    public static final StyledString PVP = StyledString.fromString(
            "Could not connect to a default or fallback server. Incorrectly configured address/port/firewall?", CKColor.RED.style
    );

    public static UnavailableMessage readFast(StyledStringReader source) {
        if (source.readAll().equals(PVP)) {
            return new UnavailableMessage(true);
        } else {
            return FAILURE;
        }
    }
}
