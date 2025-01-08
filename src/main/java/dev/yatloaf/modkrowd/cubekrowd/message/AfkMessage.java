package dev.yatloaf.modkrowd.cubekrowd.message;

import dev.yatloaf.modkrowd.cubekrowd.common.Afk;
import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

public record AfkMessage(Afk afk, StyledString name, boolean isReal) implements Message {
    public static final AfkMessage FAILURE = new AfkMessage(Afk.UNKNOWN, StyledString.EMPTY, false);

    public static AfkMessage readFast(StyledStringReader source) {
        StyledString name = source.readUntil(" ").isolate();
        if (name.isEmpty()) return FAILURE;

        source.skipUntilAfter(" ");
        Afk afk = Afk.readSuffix(source);
        if (!afk.isReal()) return FAILURE;

        return new AfkMessage(afk, name, true);
    }

    public StyledString appearance() {
        return StyledString.concat(
                this.name,
                StyledString.SPACE,
                this.afk.messageSuffix
        );
    }
}
