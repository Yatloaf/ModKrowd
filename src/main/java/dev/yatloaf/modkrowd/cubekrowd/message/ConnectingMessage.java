package dev.yatloaf.modkrowd.cubekrowd.message;

import dev.yatloaf.modkrowd.cubekrowd.common.CKColor;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subserver;
import dev.yatloaf.modkrowd.cubekrowd.subserver.Subservers;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public record ConnectingMessage(Subserver subserver, boolean isReal) implements Message {
    public static final ConnectingMessage FAILURE = new ConnectingMessage(Subservers.NONE, false);

    public static final StyledString CONNECTING_TO = StyledString.fromString("Connecting to ", CKColor.GOLD.style);
    public static final StyledString ELLIPSES = StyledString.fromString("...", CKColor.GOLD.style);

    public static ConnectingMessage readFast(StyledStringReader reader) {
        reader.skipUntilAfter(CONNECTING_TO);

        Subserver subserver = Subservers.fromListName(reader.readUntil(ELLIPSES).toUnstyledString());
        if (!subserver.isReal()) return FAILURE;

        if (!reader.skipIfNext(ELLIPSES)) return FAILURE;
        if (!reader.isAtEnd()) return FAILURE;

        return new ConnectingMessage(subserver, true);
    }

    public StyledString appearance() {
        return StyledString.concat(
                CONNECTING_TO,
                StyledString.fromString(this.subserver.listName, CKColor.GOLD.style),
                ELLIPSES
        );
    }
}
