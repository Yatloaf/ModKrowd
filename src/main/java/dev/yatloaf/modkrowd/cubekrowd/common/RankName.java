package dev.yatloaf.modkrowd.cubekrowd.common;

import dev.yatloaf.modkrowd.util.text.StyledString;
import dev.yatloaf.modkrowd.util.text.StyledStringReader;

import java.util.function.UnaryOperator;

public record RankName(Rank rank, StyledString name, boolean isReal) {
    public static final RankName FAILURE = new RankName(Rank.FAILURE, StyledString.EMPTY, false);

    public static RankName readSoft(StyledStringReader source) {
        Rank rank = Rank.readSoft(source);
        source.skipUntilAfter(" ");
        StyledString name = source.readUntil(" ").isolate();

        boolean isReal = rank.isReal() && !name.isEmpty();
        return new RankName(rank, name, isReal);
    }

    public static RankName readFast(StyledStringReader source) {
        Rank rank = Rank.readFast(source);
        if (!rank.isReal()) return FAILURE;

        source.skipUntilAfter(" ");
        StyledString name = source.readUntil(" ").isolate();
        if (name.isEmpty()) return FAILURE;

        return new RankName(rank, name, true);
    }

    public RankName mapName(UnaryOperator<StyledString> mapper) {
        return new RankName(this.rank, mapper.apply(this.name), this.isReal);
    }

    public StyledString appearance() {
        return StyledString.concat(
                this.rank.appearance(),
                StyledString.SPACE,
                this.name
        );
    }
}
